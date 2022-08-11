package com.app.base.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.base.utils.custom.NSDialogLoading
import java.util.*

/**
 * Dialog loading
 */
var dialogLoading: NSDialogLoading? = null
var timerLoading = 0L
var timerDelayLoading: Timer? = null

private fun Context.onDialogLoading(isShadown: Boolean) {

    if (dialogLoading == null) {
        timerDelayLoading?.let { it.cancel() }
        timerLoading = SystemClock.currentThreadTimeMillis()

        dialogLoading = NSDialogLoading.switcher(isShadown, false)
        dialogLoading!!.isCancelable = false
        var manager: FragmentManager? = null

        if (this is AppCompatActivity)
            manager = this.supportFragmentManager
        else
            manager = (this as Fragment).childFragmentManager

        dialogLoading!!.show(manager, "NSDialogLoading")
    }
}

fun Context.onDialogLoadingShadow() {
    onDialogLoading(true)
}

fun Context.onDialogLoadingNotShadow() {
    onDialogLoading(false)
}

fun Context.onDismissDialogLoading() {
    try {
        val timerLoad = 800L
        val endTime =
            SystemClock.currentThreadTimeMillis() - timerLoading
        if (dialogLoading != null) {
            if (endTime < timerLoad) {
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        dialogLoading!!.dismiss()
                        dialogLoading = null
                    } catch (e: Exception) {
                    }
                }, timerLoad)
            } else {
                dialogLoading!!.dismiss()
                dialogLoading = null
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}






