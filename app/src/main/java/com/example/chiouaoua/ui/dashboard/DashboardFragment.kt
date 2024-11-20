package com.example.chiouaoua.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chiouaoua.FallDetectionService
import com.example.chiouaoua.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        //service button
        val serciceStartButton: Button = binding.serviceButton
        val serviceStopButton: Button = binding.serviceStopButton


        serciceStartButton.setOnClickListener {
            val serviceIntent = Intent(requireContext(), FallDetectionService::class.java)
            requireContext().startService(serviceIntent)
            Toast.makeText(context, "Service Button Clicked!", Toast.LENGTH_SHORT).show()
        }

        serviceStopButton.setOnClickListener {
            val serviceIntent = Intent(requireContext(), FallDetectionService::class.java)
            requireContext().stopService(serviceIntent)
            Toast.makeText(context, "Service Stopped!", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}