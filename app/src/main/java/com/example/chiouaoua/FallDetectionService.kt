package com.example.chiouaoua

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat



class FallDetectionService {
    class BackgroundService : Service() {
        override fun onBind(intent: Intent?): IBinder? = null
        private val NOTIFICATION_ID = 1234
        private val CHANNEL_ID = "background_service_channel"

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            Log.d("BackgroundService", "Service started")
            // Perform background tasks here
            val notification = createNotification()

            this.startForeground(NOTIFICATION_ID, notification)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(applicationContext,
                "Service Button Clicked!",
                Toast.LENGTH_SHORT).show()
            }
            return START_STICKY
        }

        override fun onDestroy() {
            Log.d("BackgroundService", "Service stopped")
            stopForeground(true)
            super.onDestroy()

        }

        private fun createNotification(): Notification {
            // Create notification channel (if needed)
            // ...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Handler(Looper.getMainLooper()).post {
                    createNotificationChannel()
                }
            }

            return NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Background Service")
                .setContentText("Running in the background")
                .setSmallIcon(R.drawable.ic_lock_idle_alarm)
                .setOngoing(true) // Make notification persistent
                .build()
        }

        private fun createNotificationChannel() {
            val channel = NotificationChannel(CHANNEL_ID, "Background Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
}