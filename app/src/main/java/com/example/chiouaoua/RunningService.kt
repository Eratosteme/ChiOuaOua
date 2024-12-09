package com.example.chiouaoua

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.os.Vibrator
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class RunningService : Service(), SensorEventListener {
    private var isRunning: Boolean = false
    var sensorManager: SensorManager? = null
    var sensor: Sensor?=null
    var sensorListener: SensorEventListener?=null
    lateinit var text: String

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        text = "00:00:00"
        sensorManager=getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor=sensorManager!!.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun start(){
        if (!isRunning){
            sensorManager!!.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
            val notification = NotificationCompat.Builder(this, "running_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("ChiOuaOua is Active")
                .setContentText("Elapsed time: "+text)
                .setColor(ContextCompat.getColor(this, R.color.palette1_bleu))
                .setOngoing(true)
                .build()
            startForeground(1,notification)
            isRunning = true

        }
    }
    private fun stop(){
        if (isRunning) {
            isRunning = false
            stopForeground(true)
            stopSelf()
        }
    }
    enum class Actions {
        START, STOP
    }

    var xold=0
    var yold=0
    var zold=0
    var threadShould=3000.0
    var oldtime:Long=0
    override fun onSensorChanged(event: SensorEvent?) {
        if (isRunning){
            var x=event!!.values[0]
            var y=event!!.values[1]
            var z=event!!.values[2]
            var currentTime=System.currentTimeMillis()
            if((currentTime-oldtime)>100){
                var timeDiff=currentTime-oldtime
                oldtime=currentTime
                var speed=Math.abs(y+x+z-xold-yold-zold)/timeDiff*10000
                text = "$speed"
                if (speed>threadShould){
                    var v=getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    v.vibrate(500)
                    Toast.makeText(applicationContext, "shock$speed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


}