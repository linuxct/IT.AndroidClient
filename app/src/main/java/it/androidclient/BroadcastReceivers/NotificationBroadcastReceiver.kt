package it.androidclient.BroadcastReceivers

import android.app.AlarmManager
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import it.androidclient.R

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        if (intent.action != "ACTION_SNOOZE") return

        val delay = (5*60*1000).toLong()
        with(NotificationManagerCompat.from(context!!.applicationContext)) {
            cancelAll()
        }
        startAlarmBroadcastReceiver(context.applicationContext, delay)
        Toast.makeText(context.applicationContext, context.getString(R.string.notificationPostponedSuccess), Toast.LENGTH_SHORT).show()
    }

    private fun startAlarmBroadcastReceiver(context: Context, delay: Long) {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        alarmManager[AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay] = pendingIntent
    }
}