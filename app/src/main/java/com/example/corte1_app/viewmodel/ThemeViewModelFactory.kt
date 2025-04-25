package com.example.corte1_app.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Archivo necesario para pasar el contexto de la aplicación al ViewModel.

class ThemeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThemeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
