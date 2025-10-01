package com.example.navigationapp.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.navigationapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationService: LifecycleService() {
    companion object {
        const val TAG = "NotificationService"

        const val CHANNEL_ID = "notification_service_channel"
        const val NOTIFICATION_ID = 1

        const val SHOW_TIME_EXTRA = "show_time"
        const val NOTIFICATION_CLICK_URI_EXTRA = "notification_click_uri"
        const val NOTIFICATION_TITLE_EXTRA = "notification_title"
        const val NOTIFICATION_MESSAGE_EXTRA = "notification_message"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.apply {
            val showTime = getLongExtra(SHOW_TIME_EXTRA, 0L)
            val clickUrl = getStringExtra(NOTIFICATION_CLICK_URI_EXTRA)
            val title = getStringExtra(NOTIFICATION_TITLE_EXTRA)
            val message = getStringExtra(NOTIFICATION_MESSAGE_EXTRA)

            lifecycleScope.launch {
                delay(showTime)
                val notification = buildNotification(title, message ,clickUrl)
                notification?.let {
                    startForeground(NOTIFICATION_ID, it)
                }
            }
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notification Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager =  getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(title: String?, message: String?, clickUrl: String?): Notification? {
        //Only create notification if the clickUrl can open the Activity
        val intent = createNotificationIntent(clickUrl) ?: return null

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            pendingIntentFlags
        )

        return NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

//        try {
//            NotificationManagerCompat
//                .from(this)
//                .notify(NOTIFICATION_ID, builder.build())
//        } catch (e: SecurityException) {
//            Log.e(TAG, "Missing POST_NOTIFICATIONS permission", e)
//        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun createNotificationIntent(clickUrl: String?): Intent? {
        if(clickUrl.isNullOrBlank()) return null

        val intent: Intent? = try {
            Intent().apply {
                data = Uri.parse(clickUrl)
            }
        } catch (e: Exception) {
            Log.e(TAG, "", e)
            null
        }

        //Check if the intent can open the activity
        return if(intent?.resolveActivity(packageManager) != null) {
             intent
        } else {
            Log.w(TAG, "Provided clickUrl not pointing to any activity. " +
                    "Try adding clickUrl to <Queries> in Manifest ")
            null
        }
    }

}