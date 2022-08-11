package com.app.base.utils

import android.content.Context
import com.app.base.R
import com.app.base.dao.network.Repository
import com.app.base.ui.login.LoginActivity


const val container = R.id.container

fun Context.onLogin( ) {
        LoginActivity.start(this@onLogin)
}

fun presenter(): Repository {
    return Repository.getInstance()
}
