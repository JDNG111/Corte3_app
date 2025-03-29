package com.example.corte1_app.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.corte1_app.dataStore
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _profession = MutableStateFlow("")
    val profession: StateFlow<String> = _profession

    private val _nativeLanguage = MutableStateFlow("")
    val nativeLanguage: StateFlow<String> = _nativeLanguage

    private val _selectedColor = MutableStateFlow("Light")
    val selectedColor: StateFlow<String> = _selectedColor

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    private val _profilePicture = MutableStateFlow<String?>(null)
    val profilePicture: StateFlow<String?> = _profilePicture

    init {
        viewModelScope.launch { loadUserData() }
    }

    private suspend fun loadUserData() {
        context.dataStore.data.collect { prefs ->
            _username.value = prefs[stringPreferencesKey("username")] ?: ""
            _profession.value = prefs[stringPreferencesKey("profession")] ?: ""
            _nativeLanguage.value = prefs[stringPreferencesKey("nativeLanguage")] ?: ""
            _selectedColor.value = prefs[stringPreferencesKey("selectedColor")] ?: "Light"
            _notificationsEnabled.value = (prefs[stringPreferencesKey("notificationsEnabled")] ?: "true").toBoolean()
            _profilePicture.value = prefs[stringPreferencesKey("profilePicture")]
        }
    }

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
        saveToDataStore("username", newUsername)
    }

    fun updateProfession(newProfession: String) {
        _profession.value = newProfession
        saveToDataStore("profession", newProfession)
    }

    fun updateNativeLanguage(newLanguage: String) {
        _nativeLanguage.value = newLanguage
        saveToDataStore("nativeLanguage", newLanguage)
    }

    fun updateColor(color: String) {
        _selectedColor.value = color
        saveToDataStore("selectedColor", color)
    }

    fun updateNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        saveToDataStore("notificationsEnabled", enabled.toString())
    }

    fun updateProfilePicture(uri: Uri?) {
        viewModelScope.launch {
            val savedPath = uri?.let { saveImageToInternalStorage(it) }
            _profilePicture.value = savedPath
            saveToDataStore("profilePicture", savedPath ?: "")
        }
    }

    private suspend fun saveImageToInternalStorage(uri: Uri): String {
        val fileName = "profile_picture.jpg"
        val file = File(context.filesDir, fileName)

        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

        return file.absolutePath
    }

    fun deleteProfilePicture() {
        viewModelScope.launch {
            val file = File(context.filesDir, "profile_picture.jpg")
            if (file.exists()) file.delete()
            _profilePicture.value = null
            saveToDataStore("profilePicture", "")
        }
    }

    private fun saveToDataStore(key: String, value: String) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[stringPreferencesKey(key)] = value
            }
        }
    }
}
