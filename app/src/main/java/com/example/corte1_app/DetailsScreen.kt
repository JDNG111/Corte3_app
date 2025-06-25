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
import androidx.compose.material.icons.filled.LocationOn
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

            Spacer(modifier = Modifier.height(32.dp))

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

            PathConnector()

            LearningRouteButton(
                title = "AI Chat",
                description = "Practice with an AI language partner",
                icon = Icons.Default.Person,
                color = Color(0xFF607D8B),
                onClick = { navController.navigate("chatBot") }
            )

            PathConnector()

            LearningRouteButton(
                title = "Map of Engineers",
                description = "Explore engineering landmarks",
                icon = Icons.Default.LocationOn,
                color = Color(0xFF3F51B5),
                onClick = { navController.navigate("mapLearning") }
            )

            PathConnector()

            LearningRouteButton(
                title = "Forum",
                description = "Discuss topics with the community",
                icon = Icons.Default.Create,
                color = Color(0xFFCDDC39),
                onClick = { navController.navigate("forum") }
            )

            PathConnector()

            LearningRouteButton(
                title = "Listening",
                description = "Practice your listening skills",
                icon = Icons.Default.Settings,
                color = Color(0xFF8BC34A),
                onClick = { navController.navigate("listening") }
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
            .height(140.dp)  // Botones mucho más grandes para acomodar el texto
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly  // Distribuir el espacio uniformemente
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,  // Tamaño más conservador pero legible
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )

                Text(
                    text = description,
                    fontSize = 14.sp,  // Tamaño apropiado para descripciones
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 4,  // Más líneas disponibles para la descripción
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = Color.White.copy(alpha = 0.8f)
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
            .height(140.dp)  // Botones mucho más grandes para acomodar el texto
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.5f)),
        enabled = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly  // Distribuir el espacio uniformemente
            ) {
                Text(
                    text = "$title (Coming Soon)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,  // Tamaño más conservador pero legible
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )

                Text(
                    text = description,
                    fontSize = 14.sp,  // Tamaño apropiado para descripciones
                    color = Color.White.copy(alpha = 0.6f),
                    maxLines = 4,  // Más líneas disponibles para la descripción
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun PathConnector() {
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)  // Espaciado reducido para compensar botones más grandes
            .width(4.dp)
            .height(20.dp)  // Altura reducida para mejor proporción
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    )
}