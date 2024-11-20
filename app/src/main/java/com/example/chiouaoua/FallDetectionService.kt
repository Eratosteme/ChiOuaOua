package com.example.chiouaoua

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class FallDetectionService {
    class BackgroundService : Service() {
        override fun onBind(intent: Intent?): IBinder? = null

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            Log.d("BackgroundService", "Service started")
            // Perform background tasks here
            return START_STICKY
        }

        override fun onDestroy() {
            Log.d("BackgroundService", "Service stopped")
            super.onDestroy()
        }
    }
}