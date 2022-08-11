package com.app.base.ui.main

import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.app.base.R
import com.app.base.dao.model.base.UserSuitableModel
import com.app.base.ui.base.BaseFragment
import com.app.base.ui.main.adapter.Adapter
import com.newsoft.nscustomview.recyclerview.RvLayoutManagerEnums
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment(R.layout.fragment_main) {

    var isOrderFragmentSwitch = false
    var lastPositionTabSelected = 0
    lateinit var mainActivity: MainActivity
    var items: ArrayList<UserSuitableModel.Item?>? = null
    var index = 0

    override fun onCreate() {
        mainActivity = activity as MainActivity
        items = ArrayList()
    }

    override fun onViewCreated(view: View) {

        val adapter = Adapter()
        adapter.setRecyclerView(rvList, RvLayoutManagerEnums.GridLayoutManager_spanCount2)

        adapter.setLoadData {
            Log.e("setLoadData"," ")
            val data = getData()
            adapter.setItems(data, index)
            index += 10
        }
    }

    private fun getData(): ArrayList<UserSuitableModel.Item?> {
        for (i in 1..10) {
            val data = UserSuitableModel.Item()
            data.name = "text $i"
            items!!.add(data)
        }
        return items!!
    }

    private fun initTab() {
//        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
//        bottomNavigation.defaultBackgroundColor =
//            ContextCompat.getColor(requireContext(), R.color.gray5)
//        bottomNavigation.accentColor =
//            ContextCompat.getColor(requireContext(), R.color.black_blue)
//        val tab1 = AHBottomNavigationItem(
//            R.string.TAB1,
//            R.drawable.ic_showroom, R.color.black_blue
//        )
//        val tab2 =
//            AHBottomNavigationItem(
//                R.string.TAB2,
//                R.drawable.ic_member, R.color.black_blue
//            )
//        val tab3 = AHBottomNavigationItem(
//            R.string.TAB3,
//            R.drawable.ic_custommer, R.color.black_blue
//        )
//        val tab4 =
//            AHBottomNavigationItem(
//                R.string.TAB4,
//                R.drawable.ic_wallet, R.color.black_blue
//            )
//        val tab5 = AHBottomNavigationItem(
//            R.string.TAB5,
//            R.drawable.ic_bag, R.color.black_blue
//        )
//
//        val isLogin = checkLogin()
//
//        bottomNavigation.addItem(tab1)
//        if (isLogin) {
//            try {
//                AppLayerManager.getInstance().profileUserModel.info.user_type?.apply {
//                    if (code == "TTY" || code == "KT")
//                        bottomNavigation.addItem(tab2)
//                    if (code == "TTY" || code == "TST" || code == "KT")
//                        bottomNavigation.addItem(tab3)
//                    if (code != "KH")
//                        bottomNavigation.addItem(tab4)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            bottomNavigation.addItem(tab5)
//        }
//        bottomNavigation.setOnTabSelectedListener { position: Int, wasSelected: Boolean ->
//            when (bottomNavigation.getItem(position)) {
////                tab1 -> {
////                    switchFragment(ShowroomFragment(), "ShowroomFragment")
////                }
////                tab2 -> {
////                    switchFragment(MemberFragment(), "MemberFragment")
////                }
////                tab3 -> {
////                    switchFragment(CustomerFragment(), "CustomerFragment")
////                }
////                tab4 -> {
////                    switchFragment(IncomeFragment(), "IncomeFragment")
////                }
////                tab5 -> {
////                    orderFragment?.apply {
////                        EventBus.getDefault().post("PaymentFragmentReload")
////                    }
////                    orderFragment = OrderFragment.newInstance(false)
////                    switchFragment(orderFragment!!, "OrderFragment")
////                }
//            }
//            this.lastPositionTabSelected = position
//            true
//        }
//        setCurrentItem(lastPositionTabSelected)
    }

    fun setCurrentItem(currentItem: Int) {
//        bottomNavigation.currentItem = currentItem
    }

    fun setCurrentItemOrderFragment() {
//        bottomNavigation.currentItem = bottomNavigation.itemsCount - 1
    }


    private fun setNotification(count: Int) {
//        if (count == 0)
//            return
//        var size = ""
//        if (count > 100) size = " 99+ "
//        else if (count > 0) size = " $count "
//        val notification = AHNotification.Builder()
//            .setText(size)
//            .setBackgroundColor(resources.getColor(R.color.red2))
//            .setTextColor(resources.getColor(R.color.white))
//            .build()
//        try {
//            AppLayerManager.getInstance().profileUserModel.info.user_type?.apply {
//                when (code) {
//                    "TTY" -> bottomNavigation.setNotification(notification, 2)
//                    "TST" -> bottomNavigation.setNotification(notification, 1)
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("onStart", " ")
        if (isOrderFragmentSwitch) {
            setCurrentItemOrderFragment()
            isOrderFragmentSwitch = false
        }
    }

    private fun switchFragment(fragment: Fragment, tag: String) {
//        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
//        val fragment1 = childFragmentManager.findFragmentByTag(tag)
//        if (fragment1 != null && fragment1.isAdded) { // if the fragment is already in container
//            ft.show(fragment1)
//        } else { // fragment needs to be added to frame container
//            ft.add(R.id.flMain, fragment, tag)
//        }
//        val fragments = childFragmentManager.fragments
//        if (fragments != null && fragments.size > 0) {
//            for (frag in fragments) {
//                if (frag !== fragment1) {
//                    if (frag.isAdded) ft.hide(frag)
//                }
//            }
//        }
//        ft.commit()
    }

}