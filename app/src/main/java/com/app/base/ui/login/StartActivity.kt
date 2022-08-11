package com.app.base.ui.login


import android.content.Intent
import com.app.base.dao.network.exception.*
import com.app.base.ui.base.AppLayerManager
import com.app.base.ui.base.BaseActivity
import com.app.base.ui.main.MainActivity
import com.app.base.utils.presenter

class StartActivity : BaseActivity(0) {

    var isLoadApi = false
    override fun onCreate() {
//        checkVersionApp()
        startMain()
    }

    override fun onResume() {
        super.onResume()
//        checkVersionApp()
    }

    private fun onCheckToken() {
        presenter().onCheckToken().subscribeUntilDestroyNotCheckError(this, LoadingType.NOT) {
            onNext {
                AppLayerManager.getInstance().profileUserModel = it
                startMain()
            }
            onError {
                LoginActivity.start(this@StartActivity)
                finish()
//                presenter().onGetWelcomeImage()
//                    .subscribeUntilDestroyNotCheckError(this@StartActivity, LoadingType.NOT) {
//                        onNext { welcomeModel ->
//                            welcomeModel.items?.let {
//                                if (it.size > 0) {
//                                    AppLayerManager.getInstance()
//                                        .loadBitMapImage(this@StartActivity, it)
//                                    IntroduceActivity.start(this@StartActivity, toJson(it))
//                                } else
//                startMain()
//                            }
//                        }
//                        onError {
//                            startMain()
//                        }
//                    }

            }
        }
    }

    private fun startMain() {
//        startActivityExt<LoginActivity>(
////            "key_number" to 100
//        )

        startActivity(Intent(this@StartActivity, MainActivity::class.java))
        finish()
    }

    private fun checkVersionApp() {
        if (!isLoadApi) {
            isLoadApi = true
            presenter().onCheckVersion()
                .subscribeUntilDestroyNotCheckError(this, LoadingType.NOT) {
                    onNext {
                        if (it.role != "android")
                            return@onNext

                        val versionApp = packageManager.getPackageInfo(packageName, 0)
                            .versionName.replace(".", "").toInt()

                        val versionApi = it.version.replace(".", "").toInt()
                        if (versionApi > versionApp) {
                            startActivity(Intent(this@StartActivity, UpdateAppActivity::class.java))
                            finish()
//                        val dialog = AlertDialog.Builder(this@IntroduceActivity)
//                        dialog.setTitle("Thông báo")
//                        dialog.setMessage("Hiện ứng dụng đã có bản cập nhật mới nhất, vui lòng câp nhật phiên bản để có trải nghiệm tốt hơn !")
//                        dialog.setCancelable(false)
//
//                        dialog.setPositiveButton("Ok") { _, _ ->
//                            startActivity(
//                                Intent(
//                                    Intent.ACTION_VIEW,
//                                    Uri.parse("https://play.google.com/store/apps/details?id=${packageName}")
//                                )
//                            )
//                        }
//
//                        dialog.setNegativeButton("Hủy")
//                        { dialog, _ -> dialog.cancel() }
//
//                        val alert: AlertDialog = dialog.create()
//                        alert.show()
                        } else onCheckToken()
                    }
                    onError {
                        if (NetworkUtil.isOnline())
                            onCheckToken()
                        else {
//                            val alertDialogType =
//                                AlertDialogType(NoConnectivityException().message.toString())
//                            alertDialogType.isCancelable = false
//                            alertDialogType.isShowBtnClose = false
//                            showAlertDialog(alertDialogType)
                        }
                    }
                }
        }
    }


}