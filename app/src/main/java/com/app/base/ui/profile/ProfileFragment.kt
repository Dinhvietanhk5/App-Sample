package com.app.base.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.base.R
import com.app.base.ui.base.AppLayerManager
import com.app.base.ui.base.BaseFragment
import com.app.base.ui.main.MainActivity
import com.app.base.utils.container
import com.app.base.utils.presenter
import com.app.base.utils.sharePreferences.SPKeyEnum
import com.app.base.utils.sharePreferences.SharePreferencesManager
import com.newsoft.nscustom.ext.context.switchFragmentBackStack
import com.newsoft.nscustom.ext.value.copyToClipboard
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.ivAvata
import kotlinx.android.synthetic.main.fragment_profile.tvCount
import kotlinx.android.synthetic.main.view_top_home.*

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    lateinit var mainActivity: MainActivity
    var profileUserModel = AppLayerManager.getInstance().profileUserModel

    override fun onCreate() {
        mainActivity = activity as MainActivity
    }

    override fun onViewCreated(view: View) {
        tvCategory.text = "Cá nhân"
        tvCategory.gravity = Gravity.LEFT
        btnBack.setOnClickListener { mainActivity.supportFragmentManager.popBackStack() }
        btnOrder.visibility = View.GONE
        btnChangePass.setOnClickListener {
            switchFragmentBackStack(container,ChangePassFragment() )
        }

        btnProfile.setOnClickListener {
            switchFragmentBackStack(container, ChangeProfileFragment())
        }
        btnLogout.setOnClickListener {
            SharePreferencesManager.getInstance().setValue(SPKeyEnum.TOKEN, "out")
            presenter().setToken("")

//            if (AppLayerManager.getInstance().imageUrlLogin.isEmpty())
//                presenter().onGetImageAds()
//                    .subscribeUntilDestroy(requireContext(), LoadingType.SHOW_SHADOW) {
//                        onNext {
//                            AppLayerManager.getInstance().imageUrlLogin = it.items!![0].url
//                            LoginActivity.start(requireContext())
//                        }
//                    }
//            else
//                requireContext().onLogin()
        }

        btnCopyRef.setOnClickListener {
            requireContext().copyToClipboard(
                tvRefferalCode.text.toString()
            )
            Toast.makeText(context, "Đã sao chép", Toast.LENGTH_SHORT).show()
        }

        if (AppLayerManager.getInstance().totalPending > 0) {
            tvCount.visibility = View.VISIBLE
            tvCount.text = AppLayerManager.getInstance().totalPending.toString()
        } else
            tvCount.visibility = View.GONE
    }

    private fun initView() {
        try {
            profileUserModel = AppLayerManager.getInstance().profileUserModel
//            KLog.json(toJson(profileUserModel))
            tvTitle.text = profileUserModel.info.name
            tvPhone.text = profileUserModel.info.phone
            profileUserModel.info.avatarURL.let {
                ivAvata.setImageURI(it)
            }
            if (profileUserModel.info.uniqid.subSequence(0, 2) == "KH")
                viewCode.visibility = View.INVISIBLE
            else {
                tvRefferalCode.text = profileUserModel.info.uniqid
                viewCode.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

}