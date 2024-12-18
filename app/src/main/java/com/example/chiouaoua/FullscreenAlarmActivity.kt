package com.example.chiouaoua

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.SmsManager
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
//import androidx.privacysandbox.tools.core.generator.build
import com.example.chiouaoua.databinding.ActivityAlarmBinding

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenAlarmActivity : AppCompatActivity() {
    private var isRunning: Boolean = false
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var fullscreenContent: TextView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler(Looper.myLooper()!!)
    lateinit var ringtone: Ringtone

    private val PREFERENCES_FILE_KEY = "com.example.chiouaoua.preferences"
    private val NUMBER_KEY = "saved_number"
    private val MESSAGE_KEY = "saved_message"
    lateinit var savedMessage: String
    lateinit var savedNumber: String

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }
    private var isFullscreen: Boolean = false

    private val hideRunnable = Runnable { hide() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRunning = true

        // Récupérer SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

        // Charger les valeurs sauvegardées
        savedMessage = sharedPreferences.getString(MESSAGE_KEY, "").toString()
        savedNumber = sharedPreferences.getString(NUMBER_KEY, "").toString()

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri)
        ringtone.play()
        // ticking code to get out of there and make it vibrate faster and faster
        doSomethingAfterDelay(this, 1) {
            // Code to execute 40 times
            val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)) // Vibrate for 100ms

        }


        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent
        fullscreenContent.setOnClickListener { toggle() }

        fullscreenContentControls = binding.fullscreenContentControls

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        binding.buttonCancelAlarm.setOnClickListener{changeActiveActivity(this)}
        binding.buttonAlarmEarlier.setOnClickListener{alarmNow(this)}


    }

    fun doSomethingAfterDelay(context: Context, iteration: Int, action: () -> Unit) {
        val delayInMillis = 2  * 1000L // Convert seconds to milliseconds
        Handler(Looper.getMainLooper()).postDelayed({
            if (isRunning) {
                action()
                if (iteration < 21) {
                    doSomethingAfterDelay(context, iteration + 1, action)
                }
                else {
                    alarmNow(context)
                }
            }

        }, delayInMillis)
    }
    fun alarmNow(context: Context) {
        sendSMS(savedNumber, savedMessage)
        changeActiveActivity(this)
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable){}
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }
    private fun changeActiveActivity(context: Context) {
        isRunning = false
        ringtone.stop()
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
    fun sendSMS(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }
    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 1000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }
}