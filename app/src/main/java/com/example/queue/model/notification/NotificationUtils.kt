package com.example.queue.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.queue.R

class NotificationUtils {
    companion object {
        private const val CHANNEL_ID = "queue_channel_id"
        private const val CHANNEL_NAME = "Уведомление об очереди"
        private const val IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT

        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE)

                notificationChannel.description = "Queue notification channel"
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern =
                    longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        fun sendNotification(context: Context, title: String, message: String) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationID = System.currentTimeMillis().toInt()

            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notificationManager.notify(notificationID, notificationBuilder.build())
        }
    }

}