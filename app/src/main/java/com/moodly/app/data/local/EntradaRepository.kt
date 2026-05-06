// app/src/main/java/com/moodly/app/data/local/EntradaRepository.kt
package com.moodly.app.data.local

import com.moodly.app.data.model.EntradaDiaria
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class EntradaRepository(private val dao: EntradaDao) {

    fun observarTodas(): Flow<List<EntradaDiaria>> = dao.observarTodas()

    fun observarSemana(fechaEnSemana: LocalDate = LocalDate.now()): Flow<List<EntradaDiaria>> {
        val lunes  = fechaEnSemana.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val domingo = lunes.plusDays(6)
        return dao.observarSemana(lunes, domingo)
    }

    suspend fun guardar(entrada: EntradaDiaria) = dao.insertar(entrada)

    suspend fun porFecha(fecha: LocalDate): EntradaDiaria? = dao.porFecha(fecha)

    suspend fun eliminar(entrada: EntradaDiaria) = dao.eliminar(entrada)
}
