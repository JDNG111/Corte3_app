package com.example.corte1_app.screens

import android.app.TimePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.corte1_app.LoginActivity
import com.example.corte1_app.R
import com.example.corte1_app.notifications.NotificationScheduler
import com.example.corte1_app.viewmodel.ThemeViewModel
import java.io.File
import java.util.*

@Composable
fun SettingsScreen(themeViewModel: ThemeViewModel) {
    val username by themeViewModel.username.collectAsState()
    val profession by themeViewModel.profession.collectAsState()
    val nativeLanguage by themeViewModel.nativeLanguage.collectAsState()
    val selectedColor by themeViewModel.selectedColor.collectAsState()
    val notificationsEnabled by themeViewModel.notificationsEnabled.collectAsState()
    val profilePicture by themeViewModel.profilePicture.collectAsState()
    val notificationHour by themeViewModel.notificationHour.collectAsState()
    val notificationMinute by themeViewModel.notificationMinute.collectAsState()

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }
    var showImageDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { themeViewModel.updateProfilePicture(it) }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                    context.startActivity(android.content.Intent(context, LoginActivity::class.java))
                    showDialog = false
                }) { Text("Sí, cerrar sesión") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            },
            title = { Text("¿Deseas cerrar sesión?") },
            text = { Text("Esta acción te llevará de nuevo a la pantalla de inicio de sesión.") }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Foto de Perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(10.dp))

        val profileImage = remember(profilePicture) {
            profilePicture?.takeIf { it.isNotEmpty() }?.let { path ->
                File(path).takeIf { it.exists() }?.absolutePath
            } ?: R.drawable.default_profile
        }

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable { showImageDialog = true }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(profileImage).crossfade(true).build(),
                contentDescription = "Foto de perfil",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(10.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Seleccionar/Cambiar foto")
            }
            if (!profilePicture.isNullOrEmpty()) {
                Spacer(Modifier.height(10.dp))
                Button(onClick = { themeViewModel.deleteProfilePicture() }) {
                    Text("Eliminar foto")
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Mi Cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(value = username, onValueChange = { themeViewModel.updateUsername(it) }, label = { Text("Nombre de usuario") })
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = profession, onValueChange = { themeViewModel.updateProfession(it) }, label = { Text("Soy ingeniero") })
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = nativeLanguage, onValueChange = { themeViewModel.updateNativeLanguage(it) }, label = { Text("Idioma nativo") })
        Spacer(Modifier.height(10.dp))

        Text("Modo Oscuro")
        Switch(checked = selectedColor == "Dark", onCheckedChange = { themeViewModel.updateColor(if (it) "Dark" else "Light") })
        Spacer(Modifier.height(10.dp))
        Text("Notificaciones")
        Switch(checked = notificationsEnabled, onCheckedChange = { themeViewModel.updateNotifications(it) })

        Spacer(Modifier.height(10.dp))
        Text("¿Quieres fijar tu propio recordatorio?", modifier = Modifier.clickable { showTimePicker = !showTimePicker })

        if (showTimePicker) {
            Spacer(Modifier.height(8.dp))
            Text("Hora actual fijada: %02d:%02d".format(notificationHour, notificationMinute))
            Spacer(Modifier.height(4.dp))

            Button(onClick = {
                val calendar = Calendar.getInstance()
                val initialHour = notificationHour
                val initialMinute = notificationMinute

                TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        // Guardar en Firestore
                        themeViewModel.updateNotificationTime(selectedHour, selectedMinute)

                        // Programar nueva notificación
                        NotificationScheduler(context).scheduledailyNotification(selectedHour, selectedMinute)

                        // Ocultar el frame después de confirmar
                        showTimePicker = false
                    },
                    initialHour,
                    initialMinute,
                    true
                ).show()
            }) {
                Text("Confirmar notificación")
            }


            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                themeViewModel.updateNotificationTime(19, 0)
                NotificationScheduler(context).scheduledailyNotification(19, 0)
                showTimePicker = false // opcional: ocultar panel después de cancelar
            }) {
                Text("Cancelar")
            }

        }

        Spacer(Modifier.height(30.dp))
        Button(onClick = { showDialog = true }) { Text("Cerrar Sesión") }
    }

    if (showImageDialog && !profilePicture.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = { showImageDialog = false },
            confirmButton = {
                TextButton(onClick = { showImageDialog = false }) { Text("Cerrar") }
            },
            text = {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(File(profilePicture!!)).crossfade(true).build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                )
            }
        )
    }
}
