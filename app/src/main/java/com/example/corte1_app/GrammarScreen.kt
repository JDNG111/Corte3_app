package com.example.corte1_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun GrammarScreen(navController: NavController) {
    val exercises = listOf(
        "Engineers must ___ (calculate) the structural resistance before construction." to "calculate",
        "The software has been ___ (develop) to optimize processes." to "developed",
        "I have never ___ (see) such an innovative design." to "seen",
        "The prototype is being ___ (test) for durability." to "tested",
        "The voltage must be ___ (adjust) to prevent overheating." to "adjusted"
    )

    var index by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Grammar Exercises", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = exercises[index].first, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Your Answer") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            if (userAnswer.trim().equals(exercises[index].second, ignoreCase = true)) {
                feedback = "✅ Correct!"
            } else {
                feedback = "❌ Incorrect. Try again!"
            }
        }) {
            Text(text = "Check Answer")
        }

        Text(text = feedback, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (index < exercises.size - 1) {
                index++
                userAnswer = ""
                feedback = ""
            }
        }) {
            Text(text = "Next Exercise")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back to Menu")
        }
    }
}
