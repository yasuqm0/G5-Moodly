// app/src/main/java/com/moodly/app/navigation/BottomNavBar.kt
package com.moodly.app.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// ── Rutas de navegación ──────────────────────────────────────────────────────
sealed class Ruta(val ruta: String) {
    object Home      : Ruta("home")
    object Registro  : Ruta("registro")
    object Historial : Ruta("historial")
    object Recursos  : Ruta("recursos")
    object Detalle   : Ruta("detalle/{fecha}") {
        fun con(fecha: String) = "detalle/$fecha"
    }
}

data class ItemNav(
    val ruta: String,
    val label: String,
    val icono: ImageVector
)

val itemsNav = listOf(
    ItemNav(Ruta.Home.ruta,      "Inicio",   Icons.Default.Home),
    ItemNav(Ruta.Historial.ruta, "Historial",Icons.Default.List),
    ItemNav(Ruta.Recursos.ruta,  "Recursos", Icons.Default.Favorite),
)

@Composable
fun BottomNavBar(navController: NavController) {
    val backStack by navController.currentBackStackEntryAsState()
    val rutaActual = backStack?.destination?.route

    NavigationBar(
        containerColor = androidx.compose.ui.graphics.Color(0xFFFFFDF9),
        tonalElevation = androidx.compose.ui.unit.Dp(0f)
    ) {
        itemsNav.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick  = {
                    if (rutaActual != item.ruta) {
                        navController.navigate(item.ruta) {
                            popUpTo(Ruta.Home.ruta) { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                },
                icon  = { Icon(item.icono, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
