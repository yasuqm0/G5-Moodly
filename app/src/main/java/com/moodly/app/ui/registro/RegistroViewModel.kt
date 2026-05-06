// app/src/main/java/com/moodly/app/ui/registro/RegistroViewModel.kt
package com.moodly.app.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moodly.app.data.local.EntradaRepository
import com.moodly.app.data.model.EntradaDiaria
import com.moodly.app.data.model.EstadoAnimo
import com.moodly.app.data.model.Etiqueta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class RegistroUiState(
    val estado: EstadoAnimo        = EstadoAnimo.NEUTRAL,
    val nota: String               = "",
    val etiquetas: Set<Etiqueta>   = emptySet(),
    val guardado: Boolean          = false
)

class RegistroViewModel(private val repo: EntradaRepository) : ViewModel() {

    private val _state = MutableStateFlow(RegistroUiState())
    val state: StateFlow<RegistroUiState> = _state.asStateFlow()

    init {
        // Cargar entrada de hoy si ya existe
        viewModelScope.launch {
            repo.porFecha(LocalDate.now())?.let { entrada ->
                _state.value = RegistroUiState(
                    estado     = entrada.estado,
                    nota       = entrada.nota,
                    etiquetas  = entrada.etiquetas.toSet()
                )
            }
        }
    }

    fun seleccionarEstado(est: EstadoAnimo) {
        _state.value = _state.value.copy(estado = est)
    }

    fun actualizarNota(nota: String) {
        _state.value = _state.value.copy(nota = nota)
    }

    fun toggleEtiqueta(et: Etiqueta) {
        val actual = _state.value.etiquetas.toMutableSet()
        if (et in actual) actual.remove(et) else actual.add(et)
        _state.value = _state.value.copy(etiquetas = actual)
    }

    fun guardar() {
        viewModelScope.launch {
            val s = _state.value
            repo.guardar(
                EntradaDiaria(
                    fecha       = LocalDate.now(),
                    estadoNivel = s.estado.nivel,
                    nota        = s.nota.trim(),
                    etiquetas   = s.etiquetas.toList()
                )
            )
            _state.value = _state.value.copy(guardado = true)
        }
    }
}
