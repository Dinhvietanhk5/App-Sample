package com.app.base.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.app.base.R
import com.app.base.dao.network.exception.LoadingType
import com.app.base.dao.network.exception.subscribeUntilDestroy
import com.app.base.dao.network.exception.subscribeUntilDestroyCheckSuccess
import com.app.base.ui.base.AppLayerManager
import com.app.base.ui.base.BaseFragment
import com.app.base.utils.presenter
import com.app.base.utils.sharePreferences.SPKeyEnum
import com.app.base.utils.sharePreferences.SharePreferencesManager
import kotlinx.android.synthetic.main.fragment_o_t_p.*
import kotlinx.android.synthetic.main.fragment_o_t_p.btnBack
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : BaseFragment(R.layout.fragment_registration) {

    lateinit var loginActivity: LoginActivity
    var registrationType = OTPFragment.RegistrationTypeEnum.REGISTRATION

    var authToken = ""
    var phone = ""

    companion object {
        fun switch(
            type: OTPFragment.RegistrationTypeEnum,
            authToken: String,
            phone: String
        ): RegistrationFragment {
            val registrationFragment = RegistrationFragment()
            val bundle = Bundle()
            bundle.putString("phone", phone)
            bundle.putString("authToken", authToken)
            bundle.putSerializable("type", type)
            registrationFragment.arguments = bundle
            return registrationFragment
        }
    }

    override fun onCreate() {
        loginActivity = activity as LoginActivity
        requireArguments()?.let {
            phone = it.getString("phone")!!
            authToken = it.getString("authToken")!!
            registrationType =
                it.getSerializable("type") as OTPFragment.RegistrationTypeEnum
        }
    }

    override fun onViewCreated(view: View) {

        if (registrationType == OTPFragment.RegistrationTypeEnum.FORGOT_PASS) {
//            tvTitleView?.let { it.text = "Quên Mật Khẩu" }
//            tvTitlePass.text = "Mật khẩu mới"
            btnNext.text = "Thay đổi mật khẩu"
//            viewCode.visibility = View.INVISIBLE
//            tvTitleName.visibility = View.GONE
            edtName.visibility = View.GONE
            edtPass.setmImeOptions(EditorInfo.IME_ACTION_DONE)
        }

        btnBack.setOnClickListener { loginActivity.supportFragmentManager.popBackStack() }

        btnNext.setOnClickListener {
            edtPass.reset()
            val pass = edtPass.text.toString()
            if (edtPass.validate()) {
                return@setOnClickListener
            }

            if (registrationType == OTPFragment.RegistrationTypeEnum.REGISTRATION) {
                edtName.reset()
                if (edtName.validate()) {
                    return@setOnClickListener
                }

                val name = edtName.text.toString()
//                val code = edtCode.text.toString()

                presenter().onRegistry(authToken, pass, name, "", "", "")
                    .subscribeUntilDestroy(requireContext(), LoadingType.SHOW_SHADOW) {
                        onNext {
                            AppLayerManager.getInstance().profileUserModel = it
                            SharePreferencesManager.getInstance()
                                .setValue(SPKeyEnum.TOKEN, it.token)
                            SharePreferencesManager.getInstance()
                                .setValue(SPKeyEnum.PHONE, it.info!!.phone)
//                            presenter().setToken(it.token)
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                    }
            } else {
                presenter().onVerifyForgotPass(authToken, pass, phone)
                    .subscribeUntilDestroyCheckSuccess(requireContext(), LoadingType.SHOW_SHADOW) {
                        onNext {
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                    }
            }
        }
    }
}