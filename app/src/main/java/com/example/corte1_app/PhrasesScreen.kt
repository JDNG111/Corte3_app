package com.example.corte1_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PhrasesScreen(navController: NavController) {
    val phrases = listOf(
        "CRUD (Create, Read, Update, Delete) is a fundamental concept in software engineering." to
                "CRUD (Crear, Leer, Actualizar, Eliminar) es un concepto fundamental en la ingeniería de software.",

        "In civil engineering, the load-bearing capacity is crucial for structural stability." to
                "En ingeniería civil, la capacidad de carga es crucial para la estabilidad estructural.",

        "Ohm’s Law states that Voltage equals Current multiplied by Resistance (V=IR)." to
                "La Ley de Ohm establece que el Voltaje es igual a la Corriente multiplicada por la Resistencia (V=IR).",

        "Mechanical engineers must analyze material stress before manufacturing components." to
                "Los ingenieros mecánicos deben analizar el estrés del material antes de fabricar componentes.",

        "Aerodynamics is essential for designing efficient aircraft." to
                "La aerodinámica es esencial para diseñar aviones eficientes.",

        "The thermodynamic cycle determines the efficiency of an engine." to
                "El ciclo termodinámico determina la eficiencia de un motor.",

        "Robotics integrates mechanical, electrical, and software engineering." to
                "La robótica integra ingeniería mecánica, eléctrica y de software.",

        "The Agile methodology improves software development efficiency." to
                "La metodología ágil mejora la eficiencia en el desarrollo de software.",

        "Artificial Intelligence optimizes decision-making processes in automation." to
                "La Inteligencia Artificial optimiza los procesos de toma de decisiones en automatización.",

        "Engineers use computational simulations to predict system behavior." to
                "Los ingenieros usan simulaciones computacionales para predecir el comportamiento del sistema."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Phrases You Need", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(phrases) { phrase ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(text = "🔹 ${phrase.first}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "🔸 ${phrase.second}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Back to Menu")
        }
    }
}
