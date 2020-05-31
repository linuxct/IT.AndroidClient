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
    private val CHANNEL_ID = "EXERCISES"
    override fun onReceive(context: Context?, intent: Intent?) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, "Recordatorios", importance)
        mChannel.description = "Este canal representa los recordatorios de hacer tus ejercicios"
        val notificationManager = context!!.applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
            .setContentTitle("Recordatorio de tus ejercicios")
            .setContentText("¡Hola! ¿Te apetece hacer tus ejercicios diarios?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(true)
            .addAction(R.drawable.mouse_2, "¡Vamos!", pendingIntent)
            .addAction(R.drawable.mouse_2, "Posponer", snoozePendingIntent)

        with(NotificationManagerCompat.from(context.applicationContext)) {
            notify(currentNotificationId, builder.build())
        }
    }
}