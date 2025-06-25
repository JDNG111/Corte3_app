package com.example.corte1_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.corte1_app.navigation.NavGraph
import com.example.corte1_app.notifications.NotificationScheduler
import com.example.corte1_app.ui.theme.Corte1_appTheme
import com.example.corte1_app.viewmodel.ThemeViewModel
import com.example.corte1_app.viewmodel.ThemeViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var notificationScheduler: NotificationScheduler

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            setupNotifications()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        notificationScheduler = NotificationScheduler(this)
        checkAndRequestNotificationPermission()

        setContent {
            val themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModelFactory(application))
            val selectedColor by themeViewModel.selectedColor.collectAsState()
            val username by themeViewModel.username.collectAsState()
            val notificationsEnabled by themeViewModel.notificationsEnabled.collectAsState()
            val navController = rememberNavController()
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            val backgroundColor = if (selectedColor == "Dark") Color.Black else Color.White

            LaunchedEffect(notificationsEnabled) {
                if (notificationsEnabled) {
                    setupNotifications()
                } else {
                    notificationScheduler.cancelScheduledNotifications()
                }
            }

            Corte1_appTheme(darkTheme = (selectedColor == "Dark")) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        if (currentRoute?.startsWith("forumDetail") != true) {
                            Text(text = "Welcome, $username!", style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        NavGraph(context = this@MainActivity, themeViewModel, navController)
                    }
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setupNotifications()
                }
                else -> {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            setupNotifications()
        }
    }

    private fun setupNotifications() {
        notificationScheduler.scheduledailyNotification(17, 30)
    }
}
