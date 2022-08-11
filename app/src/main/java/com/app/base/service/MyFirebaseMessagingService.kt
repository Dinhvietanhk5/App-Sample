package com.app.base.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.app.base.R
import com.app.base.ui.main.MainActivity

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMsgService"

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            Log.e("onMessageReceived", "true")
            Log.e("onMessageReceived", "data" + remoteMessage.data.toString())
            Log.e("onMessageReceived", "getNotification " + remoteMessage.notification)
            showNotifi(remoteMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onMessageSent(s: String) {
        super.onMessageSent(s)
        Log.e("onMessageSent", "s:$s")
    }

    override fun handleIntent(intent: Intent) {
        Log.e(TAG, "handleIntent")
        try {
            if (intent.extras != null) {
                val builder =
                    RemoteMessage.Builder("MyFirebaseMessagingService")
                for (key in intent.extras!!.keySet())
                    builder.addData(key!!, intent.extras!![key].toString())
                onMessageReceived(builder.build())
            } else
                super.handleIntent(intent)
        } catch (e: java.lang.Exception) {
            super.handleIntent(intent)
        }
    }

    //có thông báo mới
    fun showNotifi(remoteMessage: RemoteMessage) {
        var pendingIntent: PendingIntent? = null
        val content = remoteMessage.notification!!.body

        var intent = Intent()
        intent = Intent(this, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = System.currentTimeMillis().toString()
        pendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = "english"
        val channelName = "channel_name"
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentText(content!!.toString())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(
            0 /* ID of notification */,
            notificationBuilder.build()
        )
    }
}