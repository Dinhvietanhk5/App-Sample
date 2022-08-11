package com.app.base.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.newsoft.nscustom.ext.context.checkHideKeyboardOnTouchScreen
import com.newsoft.nscustom.ext.context.hideSoftKeyboard
import com.trello.rxlifecycle.components.support.RxAppCompatActivity


abstract class BaseFragment(@LayoutRes private val layoutRes: Int) : Fragment(layoutRes) {

    var activity: RxAppCompatActivity? = null

    abstract fun onCreate()
    abstract fun onViewCreated(view: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as RxAppCompatActivity
        onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().hideSoftKeyboard()
        requireActivity().checkHideKeyboardOnTouchScreen(view)
        onViewCreated(view)
    }


}
