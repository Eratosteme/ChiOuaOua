package com.example.chiouaoua.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chiouaoua.databinding.FragmentParametersBinding

class ParametersFragment : Fragment() {

    private var _binding: FragmentParametersBinding? = null

    // Cette propriété est uniquement valide entre onCreateView et onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val parametersViewModel =
            ViewModelProvider(this).get(ParametersViewModel::class.java)

        _binding = FragmentParametersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configuration de la SeekBar
        val volumeBar = binding.volumebar
        val percentageVolume = binding.porcentagevolume

        volumeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Met à jour dynamiquement le pourcentage
                percentageVolume.text = "Volume: $progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optionnel
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optionnel
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
