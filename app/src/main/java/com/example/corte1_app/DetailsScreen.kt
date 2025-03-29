package com.example.corte1_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DetailsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Let's Practice!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate("writing") }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Writing")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { navController.navigate("vocabulary") }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Vocabulary")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { navController.navigate("readingPractice") }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Reading and Practice")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { navController.navigate("grammar") }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Grammar")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { navController.navigate("phrases") }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Phrases You Need")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Nuevos botones aún sin implementar
        Button(
            onClick = { /* No implementado aún */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Text(text = "Listening (Coming Soon)")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { /* No implementado aún */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Text(text = "IA Chat (Coming Soon)")
        }
    }
}
