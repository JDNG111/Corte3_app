package com.example.corte1_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.corte1_app.ui.theme.Corte1_appTheme
import com.example.corte1_app.LoginScreen

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Corte1_appTheme {
                Surface(modifier = Modifier) {
                    LoginScreen() // Llama a tu pantalla de login composable
                }
            }
        }
    }
}
