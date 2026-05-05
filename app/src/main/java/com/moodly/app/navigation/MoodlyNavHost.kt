// app/src/main/java/com/moodly/app/navigation/MoodlyNavHost.kt
package com.moodly.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moodly.app.data.local.EntradaRepository
import com.moodly.app.ui.detail.DetailScreen
import com.moodly.app.ui.historial.HistorialScreen
import com.moodly.app.ui.home.HomeScreen
import com.moodly.app.ui.recursos.RecursosScreen
import com.moodly.app.ui.registro.RegistroScreen

@Composable
fun MoodlyNavHost(
    navController: NavHostController,
    repository: EntradaRepository
) {
    NavHost(
        navController    = navController,
        startDestination = Ruta.Home.ruta
    ) {
        composable(Ruta.Home.ruta) {
            HomeScreen(
                repository  = repository,
                onRegistrar = { navController.navigate(Ruta.Registro.ruta) }
            )
        }

        composable(Ruta.Registro.ruta) {
            RegistroScreen(
                repository = repository,
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(Ruta.Historial.ruta) {
            HistorialScreen(
                repository = repository,
                onVerDetalle = { fecha ->
                    navController.navigate(Ruta.Detalle.con(fecha))
                }
            )
        }

        composable(
            route = Ruta.Detalle.ruta,
            arguments = listOf(navArgument("fecha") { type = NavType.StringType })
        ) { backStack ->
            val fecha = backStack.arguments?.getString("fecha") ?: return@composable
            DetailScreen(
                fecha      = fecha,
                repository = repository,
                onBack     = { navController.popBackStack() }
            )
        }

        composable(Ruta.Recursos.ruta) {
            RecursosScreen()
        }
    }
}
