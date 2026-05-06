// app/src/main/java/com/moodly/app/ui/home/HomeScreen.kt
package com.moodly.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moodly.app.data.local.EntradaRepository
import com.moodly.app.data.model.EstadoAnimo
import com.moodly.app.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    repository: EntradaRepository,
    onRegistrar: () -> Unit
) {
    val vm: HomeViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(c: Class<T>) =
                HomeViewModel(repository) as T
        }
    )
    val state by vm.uiState.collectAsStateWithLifecycle()
    val hoy = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale("es", "PE"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // ── Saludo ────────────────────────────────────────────────────────────
        Text(
            text  = hoy.format(formatter),
            style = MaterialTheme.typography.labelSmall.copy(color = TextHint)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text  = "¿Cómo estás\nhoy?",
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary
        )

        Spacer(Modifier.height(20.dp))

        // ── Grid semanal ──────────────────────────────────────────────────────
        Card(
            shape  = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Surface2),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(Modifier.padding(14.dp)) {
                Text(
                    "esta semana",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 0.5.sp,
                        fontWeight    = FontWeight.Medium
                    )
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val lunes = hoy.with(
                        java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
                    )
                    val mapEntradas = state.entradasSemana.associateBy { it.fecha }
                    (0..6).forEach { offset ->
                        val dia    = lunes.plusDays(offset.toLong())
                        val entrada = mapEntradas[dia]
                        DiaCol(
                            nombre  = dia.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale("es")),
                            emoji   = entrada?.estado?.emoji ?: "·",
                            activo  = entrada != null,
                            esHoy   = dia == hoy
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ── Chip resumen ──────────────────────────────────────────────────────
        state.estadoFrecuente?.let { est ->
            Surface(
                shape  = RoundedCornerShape(20.dp),
                color  = AccentLight
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(est.emoji, fontSize = 14.sp)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "estado más frecuente: ${est.etiqueta}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Accent, fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Botón registrar ───────────────────────────────────────────────────
        Button(
            onClick  = onRegistrar,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = Accent)
        ) {
            Text(
                if (state.yaRegistroHoy) "Editar registro de hoy" else "Registrar hoy",
                style = MaterialTheme.typography.titleMedium.copy(color = Surface)
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── Entrada reciente ──────────────────────────────────────────────────
        state.entradaReciente?.let { entrada ->
            Text(
                "entrada reciente:",
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(6.dp))
            Surface(
                shape  = RoundedCornerShape(12.dp),
                color  = Color(0xFFD6EEE6),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* navegar al detalle si se quiere */ }
            ) {
                Row(
                    Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(entrada.estado.emoji, fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            entrada.fecha.format(
                                DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale("es","PE"))
                            ).replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (entrada.nota.isNotBlank()) {
                            Text(
                                entrada.nota.take(60) + if (entrada.nota.length > 60) "…" else "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        if (entrada.etiquetas.isNotEmpty()) {
                            Spacer(Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                entrada.etiquetas.forEach { et ->
                                    EtiquetaChip(et.label)
                                }
                            }
                        }
                    }
                    Text("›", color = TextHint, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun DiaCol(nombre: String, emoji: String, activo: Boolean, esHoy: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            nombre,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (esHoy) Accent else TextHint
            )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            emoji,
            fontSize = if (activo) 22.sp else 16.sp
        )
        Spacer(Modifier.height(4.dp))
        Box(
            Modifier
                .size(5.dp)
                .clip(RoundedCornerShape(50))
                .background(if (activo) Accent else TextHint.copy(alpha = 0.3f))
        )
    }
}

@Composable
fun EtiquetaChip(label: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = TagBg
    ) {
        Text(
            label,
            Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(color = TagText, fontSize = 9.sp)
        )
    }
}