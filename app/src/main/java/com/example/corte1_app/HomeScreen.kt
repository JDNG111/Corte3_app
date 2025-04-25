package com.example.corte1_app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.min
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.corte1_app.R

//Pantalla de inicio (Home) de la app
@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val progressValues = listOf(60, 40, 75, 85, 30)
    val topics = listOf(
        "📌 Technical Vocabulary",
        "📌 Writing Engineering Reports",
        "📌 Effective Presentations",
        "📌 Understanding Technical Manuals",
        "📌 Safety & Procedures Communication"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Welcome to Engineering English!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))

            // Cargamos la imagen del logo
        Image(
            painter = painterResource(id = R.drawable.logo_appenglish),
            contentDescription = "App Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Improve your English skills with specialized vocabulary and exercises designed for engineers.",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Key Topics:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        for ((index, topic) in topics.withIndex()) {
            Text(text = "$topic - ${progressValues[index]}% completed", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(5.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Your Progress:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
            val barWidth = size.width / (progressValues.size * 2)
            progressValues.forEachIndexed { index, value ->
                drawRect(
                    color = Color.Blue,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = index * barWidth * 2 + barWidth / 2,
                        y = size.height * (1 - value / 100f)
                    ),
                    size = androidx.compose.ui.geometry.Size(barWidth, size.height * (value / 100f))
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = { navController.navigate("details") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Let's Study Now!")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { navController.navigate("settings") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(text = "My Profile")
            }
        }
    }
}
