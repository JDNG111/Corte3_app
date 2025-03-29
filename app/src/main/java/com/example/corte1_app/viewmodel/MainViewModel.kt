package com.example.corte1_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _texto = MutableLiveData("Bienvenido a la sección de detalles.")
    val texto: LiveData<String> = _texto
}
