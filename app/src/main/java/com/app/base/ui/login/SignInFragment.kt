package com.app.base.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.base.R
import com.app.base.ui.base.BaseFragment
import com.app.base.utils.container
import com.newsoft.nscustom.ext.context.switchFragmentBackStack
import kotlinx.android.synthetic.main.fragment_login.*

class SignInFragment : BaseFragment(R.layout.fragment_login) {

    lateinit var loginActivity: LoginActivity
    var countClick = 0
    override fun onCreate() {
        loginActivity = activity as LoginActivity
    }

    override fun onViewCreated(view: View) {

//     var s =   argument<String>("fjdshfj")
//Log.e("onViewCreated"," $s")

        btnForgotPass.setOnClickListener {
//            OTPFragment.switch(type)
            switchFragmentBackStack<OTPFragment>(
                container,
                "type" to OTPFragment.RegistrationTypeEnum.FORGOT_PASS
            )
//            loginActivity.onRegistrationFragment(OTPFragment.RegistrationTypeEnum.FORGOT_PASS)
        }
        btnSigin.setOnClickListener {
            loginActivity.onRegistrationFragment(OTPFragment.RegistrationTypeEnum.REGISTRATION)
        }
//
        btnLogin.setOnClickListener {
            onSigin()
        }
        edtPass.setImeOptionsListener {
            onSigin()
        }
        val versionApp =
            requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
                .versionName
//        tvVersionCode.text = "Phiên bản: $versionApp"
    }

    private fun onSigin() {
//        edtPhone.isErrorEnabled = true
//        edtPhone.setError("error")
//        edtPhone.reset()
//        edtPass.reset()

        Log.e("money ", "${edtPhone.text}")

        if (edtPhone.validate() || edtPass.validate())
            return
//        loginActivity.hideSoftKeyboard()
//        val phone = edtPhone.text.toString()
//        val pass = edtPass.text.toString()
//
//        presenter().onSigin(phone, pass)
//            .subscribeUntilDestroy(requireContext(), LoadingType.SHOW_SHADOW) {
//                onNext {
//                    SharePreferencesManager.getInstance()
//                        .setValue(SPKeyEnum.TOKEN, it.token)
//                    SharePreferencesManager.getInstance()
//                        .setValue(SPKeyEnum.PHONE, it.info!!.phone)
//                    presenter().setToken(it.token)
//                    AppLayerManager.getInstance().profileUserModel = it
////                    startActivity(Intent(context, MainActivity::class.java))
//                    loginActivity.finish()
//                }
//            }
    }

    override fun onDestroy() {
        super.onDestroy()
        countClick = 0
    }

}