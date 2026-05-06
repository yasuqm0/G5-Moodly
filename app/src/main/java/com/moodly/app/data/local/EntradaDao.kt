// app/src/main/java/com/moodly/app/data/local/EntradaDao.kt
package com.moodly.app.data.local

import androidx.room.*
import com.moodly.app.data.model.EntradaDiaria
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface EntradaDao {

    // Insertar o reemplazar entrada del día
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(entrada: EntradaDiaria)

    // Todas las entradas ordenadas por fecha descendente
    @Query("SELECT * FROM entradas ORDER BY fecha DESC")
    fun observarTodas(): Flow<List<EntradaDiaria>>

    // Entradas de una semana (lunes–domingo)
    @Query("""
        SELECT * FROM entradas
        WHERE fecha >= :inicio AND fecha <= :fin
        ORDER BY fecha DESC
    """)
    fun observarSemana(inicio: LocalDate, fin: LocalDate): Flow<List<EntradaDiaria>>

    // Una sola entrada por fecha
    @Query("SELECT * FROM entradas WHERE fecha = :fecha LIMIT 1")
    suspend fun porFecha(fecha: LocalDate): EntradaDiaria?

    // Eliminar entrada
    @Delete
    suspend fun eliminar(entrada: EntradaDiaria)
}
