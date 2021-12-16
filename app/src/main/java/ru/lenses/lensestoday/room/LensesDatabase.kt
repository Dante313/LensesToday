package ru.lenses.lensestoday.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Lenses::class],
    version = 1,
    exportSchema = false
)
abstract class LensesDatabase() : RoomDatabase() {

    abstract fun lensesDao(): LensesDao

    companion object {
        const val DATABASE_NAME = "lenses_database"
    }
}