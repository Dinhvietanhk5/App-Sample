package com.app.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.google.firebase.FirebaseApp
import com.app.base.dao.network.exception.NetworkUtil
import com.app.base.utils.sharePreferences.SharePreferencesManager
import org.greenrobot.eventbus.EventBus

class MainApp : Application() {

    // height screen design iphone xs max = 926
    // height view screen = (height view design / height screen design) x height screen

    override fun onCreate() {
        super.onCreate()
        NetworkUtil.initNetwork(this)
        SharePreferencesManager.initializeInstance(this)
        val config = ImagePipelineConfig.newBuilder(this)
            .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
            .setResizeAndRotateEnabledForNetwork(true)
            .setDownsampleEnabled(true)
            .build()
        //noinspection deprecation
        Fresco.initialize(this, config)
        FirebaseApp.initializeApp(this)
//        RealmDB.initializeInstance(this)
//        activityCall()
    }

    private fun activityCall() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
               EventBus.getDefault().post("CheckVersionApp")
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }



    var token = ""

    fun getTokenFireBase() {
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.e("TokenFireBase", "getInstanceId failed", task.exception)
//                    return@OnCompleteListener
//                }
//
//                // Get new Instance ID token
//                val token = task.result?.token
//
//                // Log and toast
//                Log.e("TokenFireBase", "token:$token")
//            })
    }

}