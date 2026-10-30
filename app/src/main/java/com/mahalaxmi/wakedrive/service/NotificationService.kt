package com.mahalaxmi.wakedrive.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mahalaxmi.wakedrive.data.models.AlertLevel
import com.mahalaxmi.wakedrive.data.models.SessionStatistics

class NotificationService : Service() {
    companion object {
        private const val CHANNEL_ID = "WakeDriveChannel"
        private const val NOTIFICATION_ID = 1
    }

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "WakeDrive Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alerts for drowsy driving detection"
            enableVibration(true)
            enableLights(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showDrowsinessAlert(alertLevel: AlertLevel, statistics: SessionStatistics) {
        val (title, message) = when (alertLevel) {
            AlertLevel.WARNING -> Pair(
                "Drowsiness Warning",
                "You're showing early signs of drowsiness. Consider taking a break."
            )
            AlertLevel.SEVERE -> Pair(
                "Severe Drowsiness Detected",
                "You appear very drowsy. Please find a safe place to stop."
            )
            AlertLevel.CRITICAL -> Pair(
                "Critical Alert!",
                "Immediate action required! Pull over safely now!"
            )
            else -> return
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    """
                    $message
                    
                    Session Statistics:
                    Total Events: ${statistics.totalEvents}
                    Drowsy Events: ${statistics.drowsyEvents}
                    Average Score: ${String.format("%.2f", statistics.averageDrowsinessScore)}
                    """.trimIndent()
                )
            )
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
