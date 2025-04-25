package com.example.corte1_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DetailsScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Text(
                text = "¡Let's Practice!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Ruta de aprendizaje
            LearningRouteButton(
                title = "Writing",
                description = "Improve your writing skills",
                icon = Icons.Default.Edit,
                color = Color(0xFF4CAF50),
                onClick = { navController.navigate("writing") }
            )

            PathConnector()

            LearningRouteButton(
                title = "Vocabulary",
                description = "Learn new words and phrases",
                icon = Icons.Default.List,
                color = Color(0xFF2196F3),
                onClick = { navController.navigate("vocabulary") }
            )

            PathConnector()

            LearningRouteButton(
                title = "Reading and Practice",
                description = "Practice reading and comprehension",
                icon = Icons.Default.Search,
                color = Color(0xFFF44336),
                onClick = { navController.navigate("readingPractice") }
            )

            PathConnector()

            LearningRouteButton(
                title = "Grammar",
                description = "Master English grammar rules",
                icon = Icons.Default.Check,
                color = Color(0xFFFF9800),
                onClick = { navController.navigate("grammar") }
            )

            PathConnector()

            LearningRouteButton(
                title = "Phrases You Need",
                description = "Essential phrases for daily use",
                icon = Icons.Default.Info,
                color = Color(0xFF9C27B0),
                onClick = { navController.navigate("phrases") }
            )

            PathConnector()

            LearningRouteButton(
                title = "My Dictionary",
                description = "Your personal word collection",
                icon = Icons.Default.Star,
                color = Color(0xFF009688),
                onClick = { navController.navigate("myDictionary") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Coming Soon",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones deshabilitados con diseño similar pero grisáceo
            DisabledLearningRouteButton(
                title = "Listening",
                description = "Practice your listening skills",
                icon = Icons.Default.Settings
            )

            Spacer(modifier = Modifier.height(16.dp))

            DisabledLearningRouteButton(
                title = "AI Chat",
                description = "Practice with an AI language partner",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LearningRouteButton(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)  // Aumentado de 80dp a 100dp
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),  // Padding interno aumentado
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),  // Icono más grande
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)  // Asegura que la columna ocupe todo el espacio disponible
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,  // Tamaño de texto aumentado
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis  // Maneja el desbordamiento con puntos suspensivos
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis  // Permite hasta 2 líneas con puntos suspensivos si es necesario
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(32.dp),  // Flecha más grande
                tint = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun DisabledLearningRouteButton(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Button(
        onClick = { /* No implementado aún */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)  // Aumentado de 80dp a 100dp
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),  // Padding interno aumentado
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.5f)),
        enabled = false
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),  // Icono más grande
                tint = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)  // Asegura que la columna ocupe todo el espacio disponible
            ) {
                Text(
                    text = "$title (Coming Soon)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,  // Tamaño de texto aumentado
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis  // Maneja el desbordamiento con puntos suspensivos
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis  // Permite hasta 2 líneas con puntos suspensivos si es necesario
                )
            }
        }
    }
}

@Composable
fun PathConnector() {
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)  // Espaciado vertical aumentado
            .width(4.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    )
}