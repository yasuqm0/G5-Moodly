// app/src/main/java/com/moodly/app/ui/recursos/RecursosScreen.kt
package com.moodly.app.ui.recursos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodly.app.ui.theme.*

// ── Datos estáticos (hardcoded según brief) ───────────────────────────────────
private data class RecursoLink(
    val emoji: String,
    val titulo: String,
    val subtitulo: String,
    val colorFondo: Color
)

private val lineasApoyo = listOf(
    RecursoLink("📞", "Línea de crisis universitaria", "ext. 4500 · 24 hrs", IconTeal),
    RecursoLink("💬", "Chat de bienestar estudiantil", "bienestar.uni.edu.pe", IconWarm),
    RecursoLink("🧑‍⚕️", "Psicología UNI", "cita: psico.uni.edu.pe", IconSoft),
)

private val articulos = listOf(
    RecursoLink("📖", "Manejo del estrés académico", "5 min de lectura", IconSoft),
    RecursoLink("🌱", "Técnicas de mindfulness", "guía rápida · PDF", IconTeal),
    RecursoLink("😴", "Higiene del sueño para uni", "3 min de lectura", IconWarm),
    RecursoLink("🤝", "Cómo pedir ayuda sin miedo", "artículo · 4 min", IconSoft),
)

// ── Composable principal ──────────────────────────────────────────────────────
@Composable
fun RecursosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // ── Encabezado ──────────────────────────────────────────────────────
        Text("Apoyo y recursos", style = MaterialTheme.typography.headlineMedium)
        Text(
            "cuida tu bienestar emocional",
            style = MaterialTheme.typography.labelSmall.copy(color = TextHint)
        )

        Spacer(Modifier.height(16.dp))

        // ── Frase motivacional ──────────────────────────────────────────────
        val lineColor = Accent
        Surface(
            shape    = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
            color    = Surface2,
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(
                        color = lineColor,
                        topLeft = Offset(0f, 0f),
                        size    = size.copy(width = 4.dp.toPx())
                    )
                }
        ) {
            Text(
                "\"Pedir ayuda no es debilidad, es el primer paso hacia el bienestar.\"",
                modifier = Modifier.padding(start = 14.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
                style    = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color    = TextSecondary
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── Sección líneas de apoyo ─────────────────────────────────────────
        SeccionLabel("líneas de apoyo")
        Spacer(Modifier.height(8.dp))
        lineasApoyo.forEach { item ->
            RecursoRow(item)
            Spacer(Modifier.height(6.dp))
        }

        Spacer(Modifier.height(12.dp))

        // ── Sección artículos ───────────────────────────────────────────────
        SeccionLabel("artículos y guías")
        Spacer(Modifier.height(8.dp))
        articulos.forEach { item ->
            RecursoRow(item)
            Spacer(Modifier.height(6.dp))
        }

        Spacer(Modifier.height(8.dp))

        // ── Mensaje de cierre ───────────────────────────────────────────────
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AccentLight,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "🌿  Recuerda: buscar apoyo es un acto de valentía.",
                Modifier.padding(14.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Accent, fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun SeccionLabel(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight    = FontWeight.SemiBold,
            letterSpacing = 0.5.sp,
            color         = TextSecondary
        )
    )
}

@Composable
private fun RecursoRow(item: RecursoLink) {
    Surface(
        shape    = RoundedCornerShape(12.dp),
        color    = Surface2,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono con fondo de color
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = item.colorFondo,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(item.emoji, fontSize = 18.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.titulo, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 13.sp))
                Text(item.subtitulo, style = MaterialTheme.typography.labelSmall.copy(color = TextHint))
            }
            Text("›", color = TextHint, fontSize = 18.sp)
        }
    }
}
