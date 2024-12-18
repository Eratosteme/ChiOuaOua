package com.example.chiouaoua.ui.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chiouaoua.databinding.FragmentParametersBinding

class ParametersFragment : Fragment() {

    private var _binding: FragmentParametersBinding? = null
    private val binding get() = _binding!!

    // Clés pour SharedPreferences
    private val PREFERENCES_FILE_KEY = "com.example.chiouaoua.preferences"
    private val MESSAGE_KEY = "saved_message"
    private val VOLUME_KEY = "saved_volume"

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParametersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Récupérer SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

        // Charger les valeurs sauvegardées
        val savedMessage = sharedPreferences.getString(MESSAGE_KEY, "")
        val savedVolume = sharedPreferences.getInt(VOLUME_KEY, 50)

        // Initialiser le champ de texte et la SeekBar
        binding.MessageEdit.setText(savedMessage)
        binding.volumebar.progress = savedVolume
        binding.porcentagevolume.text = "$savedVolume%"

        // Gestion de la SeekBar
        binding.volumebar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.porcentagevolume.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Sauvegarder la valeur de la SeekBar
                with(sharedPreferences.edit()) {
                    putInt(VOLUME_KEY, binding.volumebar.progress)
                    apply()
                }
                Toast.makeText(requireContext(), "Volume sauvegardé : ${binding.volumebar.progress}%", Toast.LENGTH_SHORT).show()
            }
        })

        // Gestion du bouton Sauvegarder pour le texte
        binding.SaveEditMessage.setOnClickListener {
            val newMessage = binding.MessageEdit.text.toString()

            if (newMessage.isNotBlank()) {
                // Sauvegarde dans SharedPreferences
                with(sharedPreferences.edit()) {
                    putString(MESSAGE_KEY, newMessage)
                    apply()
                }
                Toast.makeText(requireContext(), "Message sauvegardé !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Le champ ne peut pas être vide.", Toast.LENGTH_SHORT).show()
            }
        }

        // Gestion du bouton Tester le Volume
        binding.TestVolumeButton.setOnClickListener {
            val volume = binding.volumebar.progress

            // Utiliser ToneGenerator pour tester le volume
            val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, volume * 10) // Volume entre 0 et 100
            toneGen.startTone(ToneGenerator.TONE_DTMF_S, 500) // Joue un son pendant 500 ms
            Toast.makeText(requireContext(), "Test du volume : $volume%", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
