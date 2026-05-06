// app/src/main/java/com/moodly/app/ui/registro/RegistroScreen.kt
package com.moodly.app.ui.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.moodly.app.data.model.Etiqueta
import com.moodly.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RegistroScreen(
    repository: EntradaRepository,
    onGuardado: () -> Unit
) {
    val vm: RegistroViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(c: Class<T>) =
                RegistroViewModel(repository) as T
        }
    )
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.guardado) {
        if (state.guardado) onGuardado()
    }

    val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es", "PE"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // ── Título ─────────────────────────────────────────────────────────
        Text(
            "Registro de hoy",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            LocalDate.now().format(formatter),
            style = MaterialTheme.typography.labelSmall.copy(color = TextHint)
        )

        Spacer(Modifier.height(24.dp))

        // ── Selector de ánimo ──────────────────────────────────────────────
        Text(
            "¿cómo te sientes?",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium, letterSpacing = 0.3.sp
            )
        )
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EstadoAnimo.entries.forEach { est ->
                MoodButton(
                    est      = est,
                    selected = state.estado == est,
                    onClick  = { vm.seleccionarEstado(est) }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Nota libre ─────────────────────────────────────────────────────
        Text(
            "nota libre (opcional)",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value         = state.nota,
            onValueChange = { vm.actualizarNota(it) },
            modifier      = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 90.dp),
            placeholder   = { Text("Escribe cómo fue tu día…", color = TextHint) },
            shape         = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Accent,
                unfocusedBorderColor = Color(0x22000000),
                focusedContainerColor   = Surface2,
                unfocusedContainerColor = Surface2
            )
        )

        Spacer(Modifier.height(20.dp))

        // ── Etiquetas ──────────────────────────────────────────────────────
        Text(
            "etiquetas",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Etiqueta.entries.forEach { et ->
                val sel = et in state.etiquetas
                Surface(
                    shape    = RoundedCornerShape(8.dp),
                    color    = if (sel) Accent else Surface2,
                    modifier = Modifier
                        .border(
                            1.dp,
                            if (sel) Accent else Color(0x22000000),
                            RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { vm.toggleEtiqueta(et) }
                ) {
                    Text(
                        et.label,
                        Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (sel) Color.White else TextSecondary,
                            fontWeight = FontWeight.Medium,
                            fontSize   = 12.sp
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // ── Guardar ────────────────────────────────────────────────────────
        Button(
            onClick  = { vm.guardar() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = Accent)
        ) {
            Text(
                "Guardar entrada",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
            )
        }
    }
}

@Composable
private fun MoodButton(
    est: EstadoAnimo,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) AccentLight else Color.Transparent)
            .border(
                1.5.dp,
                if (selected) Accent else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 6.dp)
    ) {
        Text(est.emoji, fontSize = 26.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            est.etiqueta,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (selected) Accent else TextSecondary,
                fontSize = 9.sp
            )
        )
    }
}
