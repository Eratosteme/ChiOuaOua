package com.example.chiouaoua.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ParametersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Parameters Fragment"
    }
    val text: LiveData<String> = _text
}