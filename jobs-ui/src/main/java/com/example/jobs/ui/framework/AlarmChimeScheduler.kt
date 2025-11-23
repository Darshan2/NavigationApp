package com.example.jobs.ui.framework

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import com.example.common.core.utils.playRawSoundFile
import com.example.jobs.core.domain.model.TimerInterval
import com.example.jobs.ui.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import android.provider.Settings

class AlarmChimeScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val alarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    }

    fun canScheduleAlarm(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager?.canScheduleExactAlarms() == true
        } else {
            true
        }
    }

    fun askPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //To open the system’s “Allow exact alarms” settings screen where the user can toggle it manually.
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
        }
    }

    fun scheduleAlarmChimes(timerIntervals: List<TimerInterval>) {
        for(interval in timerIntervals) {
            scheduleWakeUp(interval.id.hashCode(), interval.intervalInMins)
        }
    }

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    private fun scheduleWakeUp(requestCode: Int, intervalInMinutes: Int) {
        val triggerAt = getTimeToWakeUp(intervalInMinutes)

        val intent = Intent(context, AlarmChimeReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if(canScheduleAlarm()) {
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                pendingIntent
            )
        } else {
            askPermission()
        }
    }

    private fun getTimeToWakeUp(intervalInMinutes: Int): Long {
        val now = System.currentTimeMillis()
        val triggerAt = now + intervalInMinutes * 60 * 1000L
        return triggerAt
    }

    fun cancelAlarmChimes(timerIntervals: List<TimerInterval>) {
        for(interval in timerIntervals) {
            cancelAlarmChime(interval)
        }
    }

    fun cancelAlarmChime(timerInterval: TimerInterval) {
        cancelScheduledWakeUp(timerInterval.id.hashCode())
    }

    fun cancelScheduledWakeUp(requestCode: Int) {
        val intent = Intent(context, AlarmChimeReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        pendingIntent?.let {
            alarmManager?.cancel(it)
            it.cancel()
        }
    }

}

class AlarmChimeReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        playRawSoundFile(context, R.raw.alarm_chime)
    }
}