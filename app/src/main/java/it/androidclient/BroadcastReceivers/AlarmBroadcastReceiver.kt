package it.androidclient.BroadcastReceivers

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import it.androidclient.R
import it.androidclient.Views.MainActivity
import java.time.LocalDateTime

class AlarmBroadcastReceiver : BroadcastReceiver() {
    private lateinit var CHANNEL_ID : String
    override fun onReceive(context: Context?, intent: Intent?) {
        CHANNEL_ID = context!!.getString(R.string.notificationChannelName)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, context.getString(R.string.notificationChannelCaption), importance)
        mChannel.description = context.getString(R.string.notificationChannelDesc)
        val notificationManager = context.applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        val now = LocalDateTime.now()
        val currentNotificationId = now.hour+now.minute+now.second

        val notificationIntent = Intent(context.applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("NOTIFICATION_CUSTOM_ID", currentNotificationId)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context.applicationContext, 0, notificationIntent, 0)

        val snoozeIntent = Intent(context.applicationContext, NotificationBroadcastReceiver::class.java).apply {
            action = "ACTION_SNOOZE"
            putExtra("NOTIFICATION_CUSTOM_ID", currentNotificationId)
        }
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(context.applicationContext, 0, snoozeIntent, 0)

        val builder = NotificationCompat.Builder(context.applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.mouse_2)
            .setContentTitle(context.getString(R.string.notificationTitle))
            .setContentText(context.getString(R.string.notificationContent))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(true)
            .addAction(R.drawable.mouse_2, context.getString(R.string.notificationActionOpen), pendingIntent)
            .addAction(R.drawable.mouse_2, context.getString(R.string.notificationActionPostpone), snoozePendingIntent)

        with(NotificationManagerCompat.from(context.applicationContext)) {
            notify(currentNotificationId, builder.build())
        }
    }
}