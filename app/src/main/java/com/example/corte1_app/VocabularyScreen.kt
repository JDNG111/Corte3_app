package com.example.corte1_app.ui

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FieldValue


@Composable
fun VocabularyScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    val questions = listOf(
        "1). What's the opposite of *expensive*?" to listOf("cheap", "large", "heavy"),
        "2). What does *engineer* mean?" to listOf("A person who designs and builds things", "A type of vehicle", "A cooking method"),
        "3). Which word means *a device that produces electricity*?" to listOf("Generator", "Screwdriver", "Valve")
    )

    val correctAnswers = listOf("cheap", "A person who designs and builds things", "Generator")
    val selectedAnswers = remember { mutableStateListOf("", "", "") }

    var feedback by remember { mutableStateOf("") }
    var vocabularyCompleted by remember { mutableStateOf(false) }

    // Consultar progreso al inicio
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            firestore.collection("progress")
                .document(user.uid)
                .get()
                .addOnSuccessListener { doc ->
                    vocabularyCompleted = doc.getBoolean("vocabulary_completed") == true
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(text = "Vocabulary Practice", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(10.dp))

        if (vocabularyCompleted) {
            Text(text = "✅ Ya completaste este ejercicio.", color = Color.Green)
            Spacer(modifier = Modifier.height(10.dp))
        }

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
            val isCorrect = selectedAnswers.zip(correctAnswers).all { (selected, correct) -> selected == correct }
            feedback = if (isCorrect) "✅ Correct! Well done!" else "❌ Incorrect! Try again."

            if (isCorrect && !vocabularyCompleted && currentUser != null) {
                val progress = mapOf("vocabulary_completed" to true,
                    "timestamp" to FieldValue.serverTimestamp()
                )
                firestore.collection("progress")
                    .document(currentUser.uid)
                    .set(progress, SetOptions.merge())
                vocabularyCompleted = true
            }
        }) {
            Text(text = "Submit Answers")



        Spacer(modifier = Modifier.height(20.dp))
        }

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
