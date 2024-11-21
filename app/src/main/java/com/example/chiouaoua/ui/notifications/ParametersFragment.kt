package com.example.chiouaoua.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chiouaoua.databinding.FragmentParametersBinding

class ParametersFragment : Fragment() {

    private var _binding: FragmentParametersBinding? = null
    private val binding get() = _binding!!

    // Clé pour identifier les données dans SharedPreferences
    private val PREFERENCES_FILE_KEY = "com.example.chiouaoua.preferences"
    private val MESSAGE_KEY = "saved_message"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParametersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Charger le texte sauvegardé depuis SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        val savedMessage = sharedPreferences.getString(MESSAGE_KEY, "")
        binding.MessageEdit.setText(savedMessage) // Met à jour le champ avec le texte sauvegardé

        // Bouton Sauvegarder
        binding.SaveEditMessage.setOnClickListener {
            val newMessage = binding.MessageEdit.text.toString()

            if (newMessage.isNotBlank()) {
                // Sauvegarde du message dans SharedPreferences
                with(sharedPreferences.edit()) {
                    putString(MESSAGE_KEY, newMessage)
                    apply()
                }
                // Feedback utilisateur
                Toast.makeText(requireContext(), "Message sauvegardé !", Toast.LENGTH_SHORT).show()
            } else {
                // Si le champ est vide, afficher un avertissement
                Toast.makeText(requireContext(), "Le champ ne peut pas être vide.", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}