package com.example.corte1_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ReadingPracticeScreen(navController: NavController) {
    val conversation = listOf(
        "Manager: Good morning. Are you ready for today’s production meeting?" to listOf("Yes, I have reviewed the production report.", "Not yet, I need a few more minutes."),
        "Manager: Great! What do you think is the biggest challenge in our supply chain?" to listOf("We need to optimize material logistics.", "The inventory system needs improvement."),
        "Manager: That makes sense. How can we reduce waste in production?" to listOf("By implementing lean manufacturing principles.", "By investing in better quality control."),
        "Manager: Excellent! Do you think automation could improve efficiency?" to listOf("Yes, automated processes reduce human error.", "It depends on the costs and benefits.")
    )

    var index by remember { mutableStateOf(0) }
    var selectedResponse by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(text = "Reading and Practice", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        // Display manager's line
        Text(text = conversation[index].first, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(20.dp))

        // Display response options
        conversation[index].second.forEach { response ->
            Button(
                onClick = {
                    selectedResponse = response
                    if (index < conversation.size - 1) index++
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = response)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Back to Menu")
        }
    }
}
