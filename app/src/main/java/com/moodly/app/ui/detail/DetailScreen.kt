// app/src/main/java/com/moodly/app/ui/detail/DetailScreen.kt
package com.moodly.app.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodly.app.data.local.EntradaRepository
import com.moodly.app.data.model.EntradaDiaria
import com.moodly.app.ui.home.EtiquetaChip
import com.moodly.app.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    fecha: String,
    repository: EntradaRepository,
    onBack: () -> Unit
) {
    var entrada by remember { mutableStateOf<EntradaDiaria?>(null) }
    val scope   = rememberCoroutineScope()

    LaunchedEffect(fecha) {
        scope.launch {
            entrada = repository.porFecha(LocalDate.parse(fecha))
        }
    }

    val formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM yyyy", Locale("es", "PE"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            entrada?.let { e ->
                // Emoji grande centrado
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(e.estado.emoji, fontSize = 72.sp)
                }
                Spacer(Modifier.height(8.dp))
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        e.estado.etiqueta.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Accent,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Fecha
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Surface2,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            "Fecha",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            e.fecha.format(formatter).replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Nota
                if (e.nota.isNotBlank()) {
                    Surface(
                        shape    = RoundedCornerShape(12.dp),
                        color    = Surface2,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Nota", style = MaterialTheme.typography.labelSmall)
                            Spacer(Modifier.height(4.dp))
                            Text(e.nota, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // Etiquetas
                if (e.etiquetas.isNotEmpty()) {
                    Surface(
                        shape    = RoundedCornerShape(12.dp),
                        color    = Surface2,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Etiquetas", style = MaterialTheme.typography.labelSmall)
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                e.etiquetas.forEach { et -> EtiquetaChip(et.label) }
                            }
                        }
                    }
                }

            } ?: run {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Accent)
                }
            }
        }
    }
}