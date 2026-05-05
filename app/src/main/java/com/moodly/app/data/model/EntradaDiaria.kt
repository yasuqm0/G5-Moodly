// app/src/main/java/com/moodly/app/data/model/EntradaDiaria.kt
package com.moodly.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate

@Entity(tableName = "entradas")
@TypeConverters(Converters::class)
data class EntradaDiaria(
    @PrimaryKey
    val fecha: LocalDate,          // Una entrada por día (PK = fecha)
    val estadoNivel: Int,          // 1–5 mapeado a EstadoAnimo
    val nota: String = "",
    val etiquetas: List<Etiqueta> = emptyList()
) {
    val estado: EstadoAnimo get() = EstadoAnimo.fromNivel(estadoNivel)
}

// ─── TypeConverters para Room ───────────────────────────────────────────────
class Converters {

    @TypeConverter
    fun fromLocalDate(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun toLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun fromEtiquetas(value: String?): List<Etiqueta> {
        if (value.isNullOrBlank()) return emptyList()
        return value.split(",").mapNotNull { name ->
            Etiqueta.entries.firstOrNull { it.name == name }
        }
    }

    @TypeConverter
    fun toEtiquetas(list: List<Etiqueta>?): String =
        list?.joinToString(",") { it.name } ?: ""
}
