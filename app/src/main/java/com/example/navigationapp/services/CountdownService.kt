package com.example.navigationapp.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.navigationapp.R
import com.example.navigationapp.services.NotificationService.Companion.CHANNEL_ID
import com.example.navigationapp.services.NotificationService.Companion.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CountdownService: LifecycleService() {

    companion object {
        const val COUNT_DOWN_TIME_EXTRA = "count_down_time"
        const val CLICK_URL = "click_url"
        const val NOTIFICATION_ID = 100

        const val MSG_START_TIMER: Int = 1

        const val MSG_PAUSE_TIMER: Int = 2

        const val MSG_SET_VALUE: Int = 3
    }

    private lateinit var mMessenger: Messenger
    var mClient: Messenger? = null

    var runTimer = true
    var pausedTime: Long = 0L
    var clickUrl: String = ""
    var notifCreated = false


    @SuppressLint("HandlerLeak")
    inner class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_START_TIMER -> {
                    mClient = msg.replyTo
                }
                MSG_PAUSE_TIMER -> {
                    runTimer = false
                }

                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.apply {
            val time = getLongExtra(COUNT_DOWN_TIME_EXTRA, 0L)
            clickUrl = getStringExtra(CLICK_URL) ?: ""
            startTimer(time)
        }

        return START_STICKY
    }


    fun startTimer(time: Long) {
        lifecycleScope.launch {
            for(i in time downTo 0L step 1000) {
                if(!runTimer) {
                    pausedTime = i
                    return@launch
                }
                val min = i / 60000
                val sec = i % 60000
                val title = "$min: $sec"

                if(!notifCreated) {
                    notifCreated = true
                    val notification = buildNotification(title ,clickUrl)
                    notification?.let {
                        startForeground(NotificationService.NOTIFICATION_ID, it)
                    }
//                    showNotif(title, clickUrl)
                } else {
                    mClient?.send(Message.obtain(null, MSG_SET_VALUE, title))
                    showNotif(title, clickUrl)
                }
                delay(1000)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        mMessenger = Messenger(IncomingHandler(this))
        return mMessenger.binder
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

    private fun buildNotification(title: String?, clickUrl: String?): Notification? {
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
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

    }

    private fun showNotif(clickUrl: String?, title: String?) {
        val intent = createNotificationIntent(clickUrl) ?: return

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

        val builder =  NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat
                .from(this)
                .notify(NOTIFICATION_ID, builder.build())
        } catch (e: SecurityException) {
            Log.e(TAG, "Missing POST_NOTIFICATIONS permission", e)
        }
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
            Log.w(
                TAG, "Provided clickUrl not pointing to any activity. " +
                    "Try adding clickUrl to <Queries> in Manifest ")
            null
        }
    }
}