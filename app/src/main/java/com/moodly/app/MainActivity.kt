// app/src/main/java/com/moodly/app/MainActivity.kt
package com.moodly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moodly.app.data.local.EntradaRepository
import com.moodly.app.data.local.MoodlyDatabase
import com.moodly.app.navigation.BottomNavBar
import com.moodly.app.navigation.MoodlyNavHost
import com.moodly.app.navigation.Ruta
import com.moodly.app.ui.theme.MoodlyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Room y repositorio (sin DI para simplificar)
        val db         = MoodlyDatabase.obtener(applicationContext)
        val repository = EntradaRepository(db.entradaDao())

        setContent {
            MoodlyTheme {
                val navController = rememberNavController()
                val backStack by navController.currentBackStackEntryAsState()
                val rutaActual   = backStack?.destination?.route

                // Ocultar bottom bar en pantalla de detalle
                val mostrarBar = rutaActual != Ruta.Detalle.ruta &&
                        !rutaActual.orEmpty().startsWith("detalle/")

                Scaffold(
                    bottomBar = {
                        if (mostrarBar) BottomNavBar(navController)
                    }
                ) { innerPadding ->
                    MoodlyNavHost(
                        navController = navController,
                        repository    = repository,
                        // Modifier para respetar el padding del Scaffold
                    )
                }
            }
        }
    }
}
