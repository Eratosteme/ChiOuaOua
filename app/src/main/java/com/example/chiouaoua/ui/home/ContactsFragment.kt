package com.example.chiouaoua.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
//import androidx.compose.ui.semantics.text
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chiouaoua.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val PREFERENCES_FILE_KEY = "com.example.chiouaoua.preferences"
    private val NUMBER_KEY = "saved_number"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contactsViewModel =
            ViewModelProvider(this).get(ContactsViewModel::class.java)

        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Récupérer SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

        // Charger les valeurs sauvegardées
        val savedMessage = sharedPreferences.getString(NUMBER_KEY, "")

        // Initialiser le champ de texte et la SeekBar
        binding.MessageEdit.setText(savedMessage)
        val saveButton: Button = binding.SaveEditMessage
        val textView: TextView = binding.MessageEdit


        saveButton.setOnClickListener {
            val editedMessage = textView.text.toString()
            if (editedMessage.isNotBlank()) {
                // Sauvegarde dans SharedPreferences
                with(sharedPreferences.edit()) {
                    putString(NUMBER_KEY, editedMessage)
                    apply()
                }
                Toast.makeText(requireContext(), "numéro sauvegardé !", Toast.LENGTH_SHORT).show()
            } else {
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