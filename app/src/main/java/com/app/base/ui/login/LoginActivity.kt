package com.app.base.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.app.base.R
import com.app.base.ui.base.BaseActivity
import com.app.base.utils.container
import com.newsoft.nscustom.ext.context.switchFragmentBackStack
import com.newsoft.nscustom.ext.context.switchFragmentNotBackStack


class LoginActivity : BaseActivity(R.layout.activity_login) {

    lateinit var signInFragment: SignInFragment

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate() {
        getTokenFirebase()

//        var data = getDataExtra<String>("jdshf")
//        Log.e("data", "$data")
//        val isIntroduct =
//            SharePreferencesManager.getInstance().getBoolean(SPKeyEnum.INTRODUCE_VIEW, false)
//        if (!isIntroduct!!) {
//            SharePreferencesManager.getInstance().setValue(SPKeyEnum.INTRODUCE_VIEW, true)
//            switchFragment(ChoseTypeAccFragment(), "ChoseTypeAccFragment", false)
//        } else
        onLogin()
    }

    private fun onLogin() {
        signInFragment = SignInFragment()
        switchFragmentNotBackStack(container, signInFragment)
    }

    fun onRegistrationFragment(type: OTPFragment.RegistrationTypeEnum) {
//        switchFragmentBackStack()
//        switchFragment(OTPFragment.switch(type), "RegistrationFragment", true)
    }

    fun onSettingFragment(
        oTPType: OTPFragment.RegistrationTypeEnum,
        auth_token: String,
        phone: String
    ) {
//        switchFragment(
//            RegistrationFragment.switch(oTPType, auth_token, phone),
//            "OTPFragment",
//            true
//        )
    }

}
