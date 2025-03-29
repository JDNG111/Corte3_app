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

@Composable
fun VocabularyScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val questions = listOf(
        "1). What's the opposite of *expensive*?" to listOf("cheap", "large", "heavy"),
        "2). What does *engineer* mean?" to listOf("A person who designs and builds things", "A type of vehicle", "A cooking method"),
        "3). Which word means *a device that produces electricity*?" to listOf("Generator", "Screwdriver", "Valve")
    )

    val correctAnswers = listOf("cheap", "A person who designs and builds things", "Generator")

    val selectedAnswers = remember { mutableStateListOf("", "", "") }
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(text = "Vocabulary Practice", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(10.dp))

        for ((index, question) in questions.withIndex()) {
            Text(text = question.first, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(5.dp))

            question.second.forEach { answer ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedAnswers[index] == answer,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedAnswers[index] = answer
                            } else {
                                selectedAnswers[index] = ""
                            }
                        }
                    )
                    Text(text = answer, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
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
