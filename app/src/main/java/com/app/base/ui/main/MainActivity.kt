package com.app.base.ui.main


import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.app.base.R
import com.app.base.dao.network.exception.LoadingType
import com.app.base.dao.network.exception.subscribeUntilDestroyNotCheckError
import com.app.base.ui.base.BaseActivity
import com.app.base.ui.login.UpdateAppActivity
import com.app.base.ui.profile.ChangeProfileFragment
import com.app.base.utils.presenter
import com.newsoft.nscustom.ext.context.switchFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.system.exitProcess


class MainActivity : BaseActivity(R.layout.activity_main) {


    //https://github.com/dinuscxj/RecyclerRefreshLayout
    lateinit var mainFragment: MainFragment
    var changeProfileFragment: ChangeProfileFragment? = null
    var doubleBackToExitPressedOnce = false

    override fun onCreate() {
        mainFragment = MainFragment()
        switchFragment(container, mainFragment)
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)

//        val test = intent.getDataExtras("key_content", 0)
//        val number = intent.getDataExtras("key_number", 0)
//        Log.e("test", "$test $number")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventBusPush(conten: String) {
        if (conten == "CheckVersionApp")
            checkVersionApp()
    }

    private fun checkVersionApp() {
        presenter().onCheckVersion()
            .subscribeUntilDestroyNotCheckError(this, LoadingType.NOT) {
                onNext {
                    if (it.role != "android")
                        return@onNext

                    val versionApp = packageManager.getPackageInfo(packageName, 0)
                        .versionName.replace(".", "").toInt()

                    val versionApi = it.version.replace(".", "").toInt()
                    if (versionApi > versionApp) {
                        startActivity(Intent(this@MainActivity, UpdateAppActivity::class.java))
                        finish()
                    }
                }
            }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 0) {
            if (doubleBackToExitPressedOnce) {
                finish()
                exitProcess(0)
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Nhấn back 2 lần để thoát !", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                kotlin.run {
                    doubleBackToExitPressedOnce = false
                }
            }, 3000)
        } else
            supportFragmentManager.popBackStack()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}