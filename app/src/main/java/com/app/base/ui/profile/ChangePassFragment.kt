package com.app.base.ui.profile

import android.view.Gravity
import android.view.View
import com.app.base.R
import com.app.base.dao.network.exception.LoadingType
import com.app.base.dao.network.exception.subscribeUntilDestroyCheckSuccess
import com.app.base.ui.base.BaseFragment
import com.app.base.ui.main.MainActivity
import com.app.base.utils.presenter
import com.newsoft.nscustom.ext.context.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_change_pass.*
import kotlinx.android.synthetic.main.fragment_change_pass.edtPass
import kotlinx.android.synthetic.main.fragment_change_pass.edtPass2
import kotlinx.android.synthetic.main.view_top_home.*

class ChangePassFragment : BaseFragment(R.layout.fragment_notification) {

    lateinit var mainActivity: MainActivity

    override fun onCreate() {
        mainActivity = activity as MainActivity
    }

    override fun onViewCreated(view: View) {
        tvCategory.text = "Thay đổi mật khẩu"
        tvCategory.gravity = Gravity.LEFT

        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        btnSave.setOnClickListener {
            edtPassOld.reset()
            edtPass.reset()
            edtPass2.reset()
            val isEdtPassOld = edtPassOld.validate()
            val isEdtPass = edtPass.validate()
            val isEdtPass2 = edtPass2.validatePass(edtPass.text.toString())

            mainActivity.hideSoftKeyboard()
            if (isEdtPass || isEdtPass2 || isEdtPassOld)
                return@setOnClickListener

            val passOld = edtPassOld.text.toString()
            val passNew = edtPass.text.toString()
            presenter().onChangePassword(passOld, passNew)
                .subscribeUntilDestroyCheckSuccess(requireContext(), LoadingType.SHOW_SHADOW)
                {
                    onNext {
                        mainActivity.supportFragmentManager.popBackStack()
                    }
                }
        }

    }

}