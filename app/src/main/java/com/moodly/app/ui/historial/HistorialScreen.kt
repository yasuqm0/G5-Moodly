// app/src/main/java/com/moodly/app/ui/historial/HistorialScreen.kt
package com.moodly.app.ui.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moodly.app.data.local.EntradaRepository
import com.moodly.app.data.model.EntradaDiaria
import com.moodly.app.ui.home.EtiquetaChip
import com.moodly.app.ui.theme.Accent
import com.moodly.app.ui.theme.AccentLight
import com.moodly.app.ui.theme.Background
import com.moodly.app.ui.theme.Surface2
import com.moodly.app.ui.theme.TextHint
import com.moodly.app.ui.theme.TextSecondary
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistorialScreen(
    repository: EntradaRepository,
    onVerDetalle: (String) -> Unit
) {
    val vm: HistorialViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                HistorialViewModel(repository) as T
        }
    )
    val state by vm.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // ── Header con navegación de semana ──────────────────────────────
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                "Historial",
                style = MaterialTheme.typography.headlineMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { vm.semanaAnterior() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Semana anterior",
                        tint = TextSecondary
                    )
                }
                Text(
                    "sem ${state.numeroSemana}",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
                IconButton(onClick = { vm.semanaSiguiente() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Semana siguiente",
                        tint = TextSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ── Banner resumen semanal ────────────────────────────────────────
        state.predominante?.let { est ->
            Surface(
                shape    = RoundedCornerShape(12.dp),
                color    = AccentLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(est.emoji, fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "Predominó: ${est.etiqueta}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color      = Accent,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        val count = state.entradas.count { it.estado == est }
                        Text(
                            "$count de ${state.entradas.size} días registrados",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── Lista de entradas ─────────────────────────────────────────────
        if (state.entradas.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Sin entradas esta semana.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.entradas) { entrada ->
                    EntradaItem(
                        entrada = entrada,
                        onClick = { onVerDetalle(entrada.fecha.toString()) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EntradaItem(entrada: EntradaDiaria, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale("es", "PE"))

    Surface(
        shape    = RoundedCornerShape(12.dp),
        color    = Surface2,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(entrada.estado.emoji, fontSize = 30.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    entrada.fecha.format(formatter).replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp)
                )
                if (entrada.nota.isNotBlank()) {
                    Text(
                        entrada.nota.take(55) + if (entrada.nota.length > 55) "…" else "",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp)
                    )
                }
                if (entrada.etiquetas.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        entrada.etiquetas.forEach { et -> EtiquetaChip(et.label) }
                    }
                }
            }
            Text("›", color = TextHint, fontSize = 18.sp)
        }
    }
}