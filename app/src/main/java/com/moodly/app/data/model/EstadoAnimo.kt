// app/src/main/java/com/moodly/app/data/model/EstadoAnimo.kt
package com.moodly.app.data.model

enum class EstadoAnimo(
    val nivel: Int,
    val etiqueta: String,
    val emoji: String
) {
    MUY_MAL (1, "muy mal",   "😣"),
    MAL     (2, "mal",       "😔"),
    NEUTRAL (3, "neutral",   "😐"),
    BIEN    (4, "bien",      "🙂"),
    EXCELENTE(5, "excelente","😄");

    companion object {
        fun fromNivel(n: Int) = entries.firstOrNull { it.nivel == n } ?: NEUTRAL
    }
}

enum class Etiqueta(val label: String) {
    ESTRES   ("estrés"),
    SUENO    ("sueño"),
    ACADEMICO("académico"),
    SOCIAL   ("social");
}
