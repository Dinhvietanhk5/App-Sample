package com.app.base.ui.login

import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.app.base.R
import com.app.base.dao.network.exception.LoadingType
import com.app.base.dao.network.exception.subscribeUntilDestroy
import com.app.base.ui.base.BaseFragment
import com.app.base.utils.presenter
import com.newsoft.nscustom.ext.context.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_o_t_p.*

class OTPFragment : BaseFragment(R.layout.fragment_o_t_p) {

    enum class RegistrationTypeEnum {
        REGISTRATION,
        FORGOT_PASS
    }

    lateinit var loginActivity: LoginActivity
    var registrationType = RegistrationTypeEnum.REGISTRATION
    var session = ""
    override fun onCreate() {
        loginActivity = activity as LoginActivity
        requireArguments()?.let {
            registrationType = it.getSerializable("type") as RegistrationTypeEnum
        }
    }

    override fun onViewCreated(view: View) {

        if (registrationType == RegistrationTypeEnum.FORGOT_PASS) {
            tvCategory.text = "Quên Mật Khẩu"
        }
        viewOtp.visibility = View.GONE

        btnBack.setOnClickListener { loginActivity.supportFragmentManager.popBackStack() }

        btnReloadOtp.setOnClickListener {
            onSenOtp()
        }
        edtPhone.setImeOptionsListener {
            onNext()
        }

        btnNext.setOnClickListener {
            onNext()
        }

        edtOtp.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            onNext()
            false
        }
    }

    private fun onSenOtp() {
        edtPhone.reset()
        val isEdtPhone = edtPhone.validate()
        loginActivity.hideSoftKeyboard()
        if (isEdtPhone)
            return

        presenter().onSendOTP(edtPhone.text.toString())
            .subscribeUntilDestroy(requireContext(), LoadingType.SHOW_SHADOW) {
                onNext {
                    session = it.session
                    viewOtp.visibility = View.VISIBLE
                }
            }
    }

    private fun onNext() {
        val isEdtOtp = edtOtp.text.toString().isEmpty()
        if (isEdtOtp)
            return

        presenter().onOTPVertify(session, edtOtp.text.toString())
            .subscribeUntilDestroy(requireContext(), LoadingType.SHOW_SHADOW) {
                onNext {
                    loginActivity.supportFragmentManager.popBackStack()
                    loginActivity.onSettingFragment(
                        registrationType,
                        it.auth_token,
                        edtPhone.text.toString()
                    )
                }
            }
    }

}