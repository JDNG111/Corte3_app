package com.example.corte1_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

//Pantalla de ejercicio de ingles
@Composable
fun WritingScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val options = listOf("problems", "machines", "language")
    val correctAnswers = listOf("problems", "machines", "language")
    val selectedAnswers = remember { mutableStateListOf("", "", "") }
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(text = "Technical English Writing Practice", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Complete the sentences with the correct words:", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Engineering is the application of science and mathematics to solve _____. " +
                    "Engineers design and build _____ that improve our daily lives. " +
                    "In order to communicate effectively, engineers must use precise _____.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        for (i in selectedAnswers.indices) {
            var expanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { expanded = true }) {
                    Text(text = if (selectedAnswers[i].isEmpty()) "Select Answer" else selectedAnswers[i])
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    options.forEach { answer ->
                        DropdownMenuItem(text = { Text(answer) }, onClick = {
                            selectedAnswers[i] = answer
                            expanded = false
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            feedback = if (selectedAnswers.zip(correctAnswers).all { (selected, correct) -> selected == correct }) {
                "✅ Correct! Well done!"
            } else {
                "❌ Incorrect! Try again."
            }
        }) {
            Text(text = "Submit Answers")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (feedback.isNotEmpty()) {
            Text(
                text = feedback,
                style = MaterialTheme.typography.bodyLarge,
                color = if (feedback.contains("Correct")) Color.Green else Color.Red
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back to Menu")
        }
    }
}
