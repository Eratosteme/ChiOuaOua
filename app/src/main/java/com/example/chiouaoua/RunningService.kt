package com.example.chiouaoua

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class RunningService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun start(){
        val notification = NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("ChiOuaOua is Active")
            .setContentText("Elapsed time: 00:50")
            .setColor(ContextCompat.getColor(this, R.color.palette1_bleu))
            .setOngoing(true)
            .build()
        startForeground(1,notification)
    }
    enum class Actions {
        START, STOP
    }
}