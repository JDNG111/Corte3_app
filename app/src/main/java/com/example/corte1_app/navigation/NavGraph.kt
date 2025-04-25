package com.example.corte1_app.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.corte1_app.ui.*
import com.example.corte1_app.viewmodel.ThemeViewModel
import com.example.corte1_app.screens.SettingsScreen


@Composable
fun NavGraph(context: Context, themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        // Navegación Principal
        composable("home") { HomeScreen(navController) }
        composable("details") { DetailsScreen(navController) }
        composable("settings") { SettingsScreen(themeViewModel) }

        // Navegación accesible para los ejercicios de practica
        composable("writing") { WritingScreen(navController) }
        composable("vocabulary") { VocabularyScreen(navController) }
        composable("readingPractice") { ReadingPracticeScreen(navController) }
        composable("grammar") { GrammarScreen(navController) }
        composable("phrases") { PhrasesScreen(navController) }
        composable("myDictionary") { MyDictionaryScreen(navController) }


    }
}
