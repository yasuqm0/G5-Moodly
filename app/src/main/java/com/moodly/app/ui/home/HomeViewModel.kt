// app/src/main/java/com/moodly/app/ui/home/HomeViewModel.kt
package com.moodly.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moodly.app.data.local.EntradaRepository
import com.moodly.app.data.model.EntradaDiaria
import com.moodly.app.data.model.EstadoAnimo
import kotlinx.coroutines.flow.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

data class HomeUiState(
    val entradasSemana: List<EntradaDiaria> = emptyList(),
    val entradaReciente: EntradaDiaria?     = null,
    val estadoFrecuente: EstadoAnimo?       = null,
    val yaRegistroHoy: Boolean              = false
)

class HomeViewModel(private val repo: EntradaRepository) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = repo
        .observarSemana(LocalDate.now())
        .map { lista ->
            val hoy      = LocalDate.now()
            val reciente = lista.firstOrNull()
            val frecuente = lista
                .groupBy { it.estado }
                .maxByOrNull { it.value.size }
                ?.key
            HomeUiState(
                entradasSemana = lista,
                entradaReciente = reciente,
                estadoFrecuente = frecuente,
                yaRegistroHoy   = lista.any { it.fecha == hoy }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    // Mapa fecha→entrada para el grid semanal
    val diasSemana: List<Pair<LocalDate, EntradaDiaria?>>
        get() {
            val lunes = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val map   = uiState.value.entradasSemana.associateBy { it.fecha }
            return (0..6).map { offset ->
                val d = lunes.plusDays(offset.toLong())
                d to map[d]
            }
        }
}
