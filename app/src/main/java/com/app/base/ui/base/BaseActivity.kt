package com.app.base.ui.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import com.newsoft.nscustom.ext.context.hideSoftKeyboard
import com.newsoft.nscustom.ext.context.setTransparentActivity
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

abstract class BaseActivity(@LayoutRes private val layoutRes: Int) : RxAppCompatActivity() {

    abstract fun onCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutRes != 0) setContentView(layoutRes)
        setTransparentActivity()
        hideSoftKeyboard()
        onCreate()
    }

    /**
     * check hide keyboard on touch screen
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun getTokenFirebase() {
//        if (!TextUtils.isEmpty(AppLayerManager.getInstance().tokenFirebase))
//            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful)
//                    return@OnCompleteListener
//                // Get new FCM registration token
//                val token = task.result
//                AppLayerManager.getInstance().tokenFirebase = token!!
//                Log.e("token FCM", token)
//            })
    }

    override fun onDestroy() {
        super.onDestroy()
        hideSoftKeyboard()
    }

}