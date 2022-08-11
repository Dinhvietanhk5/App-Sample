package com.app.base.ui.login


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.app.base.R
import com.app.base.ui.base.BaseActivity
import com.newsoft.nscustom.ext.context.openAppInPlayStore
import kotlinx.android.synthetic.main.activity_update_app.*

class UpdateAppActivity : BaseActivity(R.layout.activity_update_app) {

    override fun onCreate() {
        btnUpdate.setOnClickListener {
            openAppInPlayStore()
        }
    }

}