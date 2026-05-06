// app/src/main/java/com/moodly/app/data/local/MoodlyDatabase.kt
package com.moodly.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moodly.app.data.model.Converters
import com.moodly.app.data.model.EntradaDiaria

@Database(entities = [EntradaDiaria::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MoodlyDatabase : RoomDatabase() {

    abstract fun entradaDao(): EntradaDao

    companion object {
        @Volatile private var INSTANCE: MoodlyDatabase? = null

        fun obtener(context: Context): MoodlyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MoodlyDatabase::class.java,
                    "moodly_db"
                ).build().also { INSTANCE = it }
            }
    }
}
