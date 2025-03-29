package com.example.corte1_app.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.corte1_app.viewmodel.ThemeViewModel
import com.example.corte1_app.R
import java.io.File

@Composable
fun SettingsScreen(themeViewModel: ThemeViewModel) {
    val username by themeViewModel.username.collectAsState()
    val profession by themeViewModel.profession.collectAsState()
    val nativeLanguage by themeViewModel.nativeLanguage.collectAsState()
    val selectedColor by themeViewModel.selectedColor.collectAsState()
    val notificationsEnabled by themeViewModel.notificationsEnabled.collectAsState()
    val profilePicture by themeViewModel.profilePicture.collectAsState()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { themeViewModel.updateProfilePicture(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Foto de Perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(10.dp))

        val profileImage = remember(profilePicture) {
            profilePicture?.takeIf { it.isNotEmpty() }?.let { path ->
                File(path).takeIf { it.exists() }?.absolutePath
            } ?: R.drawable.default_profile
        }

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(profileImage)
                .crossfade(true)
                .build(),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Seleccionar/Cambiar foto")
            }
            if (!profilePicture.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { themeViewModel.deleteProfilePicture() }) {
                    Text("Eliminar foto")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Mi Cuenta", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { themeViewModel.updateUsername(it) },
            label = { Text("Nombre de usuario") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = profession,
            onValueChange = { themeViewModel.updateProfession(it) },
            label = { Text("Soy ingeniero") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = nativeLanguage,
            onValueChange = { themeViewModel.updateNativeLanguage(it) },
            label = { Text("Idioma nativo") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Modo Oscuro")
        Switch(
            checked = selectedColor == "Dark",
            onCheckedChange = { isChecked ->
                val newMode = if (isChecked) "Dark" else "Light"
                themeViewModel.updateColor(newMode)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Notificaciones")
        Switch(
            checked = notificationsEnabled,
            onCheckedChange = { themeViewModel.updateNotifications(it) }
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}
