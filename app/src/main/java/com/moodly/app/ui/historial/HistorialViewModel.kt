// app/src/main/java/com/moodly/app/ui/historial/HistorialViewModel.kt
package com.moodly.app.ui.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moodly.app.data.local.EntradaRepository
import com.moodly.app.data.model.EntradaDiaria
import com.moodly.app.data.model.EstadoAnimo
import kotlinx.coroutines.flow.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.IsoFields
import java.time.temporal.TemporalAdjusters

data class HistorialUiState(
    val entradas: List<EntradaDiaria> = emptyList(),
    val predominante: EstadoAnimo?    = null,
    val semanaActual: LocalDate       = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
    val numeroSemana: Int             = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
)

class HistorialViewModel(private val repo: EntradaRepository) : ViewModel() {

    private val _semana = MutableStateFlow(
        LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    )

    val uiState: StateFlow<HistorialUiState> = _semana
        .flatMapLatest { lunes ->
            repo.observarSemana(lunes)
                .map { lista ->
                    val pred = lista
                        .groupBy { it.estado }
                        .maxByOrNull { it.value.size }
                        ?.key
                    HistorialUiState(
                        entradas     = lista,
                        predominante = pred,
                        semanaActual = lunes,
                        numeroSemana = lunes.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                    )
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistorialUiState())

    fun semanaAnterior() {
        _semana.value = _semana.value.minusWeeks(1)
    }

    fun semanaSiguiente() {
        val siguiente = _semana.value.plusWeeks(1)
        if (!siguiente.isAfter(LocalDate.now())) {
            _semana.value = siguiente
        }
    }
}
