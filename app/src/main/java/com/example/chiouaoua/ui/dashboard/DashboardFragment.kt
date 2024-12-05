package com.example.chiouaoua.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chiouaoua.R
import com.example.chiouaoua.RunningService
import com.example.chiouaoua.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    //--------------------Variables pour le chronomètre---------------------------------------------

    private lateinit var timerTextView: TextView
    private lateinit var launchServiceButton: Button
    private lateinit var stopServiceButton: Button
    private lateinit var resetButton: Button

    private var isRunning = false
    private var startTime = 0L
    private var timeInMilliseconds = 0L
    private var timeSwapBuff = 0L
    private var updatedTime = 0L

    private val handler = Handler()

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updatedTime = timeSwapBuff + timeInMilliseconds

            val secs = (updatedTime / 1000).toInt()
            val mins = secs / 60
            val hrs = mins / 60

            timerTextView.text = String.format("%02d:%02d:%02d", hrs, mins % 60, secs % 60)
            handler.postDelayed(this, 0)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val serciceStartButton: Button = binding.serviceButton
        val serviceStopButton: Button = binding.serviceStopButton

        timerTextView = binding.timerTextView
        launchServiceButton = binding.serviceButton
        stopServiceButton = binding.serviceStopButton
        resetButton = binding.resetButton

        // ----------------Lancer le service et démarrer le chronomètre-----------------------------

        launchServiceButton.setOnClickListener {
            val serviceIntent = Intent(requireContext(), RunningService::class.java)
            serviceIntent.action = RunningService.Actions.START.toString()
            ContextCompat.startForegroundService(requireContext(), serviceIntent)
            Toast.makeText(context, "Service Started!", Toast.LENGTH_SHORT).show()

            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(updateTimerThread, 0)
            isRunning = true
        }

        // -----------------Arreter le service et mettre en pause le chronomètre--------------------

        stopServiceButton.setOnClickListener {
            val serviceIntent = Intent(requireContext(), RunningService::class.java)
            serviceIntent.action = RunningService.Actions.STOP.toString()
            ContextCompat.startForegroundService(requireContext(), serviceIntent)
            Toast.makeText(context, "Service Stopped!", Toast.LENGTH_SHORT).show()

            if (isRunning) {
                timeSwapBuff += timeInMilliseconds
                handler.removeCallbacks(updateTimerThread)
                isRunning = false
            }
        }

        // -----------------Reset le chronomètre----------------------------------------------------

        resetButton.setOnClickListener {
            if (!isRunning) {
                startTime = 0L
                timeInMilliseconds = 0L
                timeSwapBuff = 0L
                updatedTime = 0L
                timerTextView.text = "00:00:00"
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}