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
        @Volatile
        private var INSTANCE: LensesDatabase? = null

        fun getDatabase(context: Context): LensesDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LensesDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }

        private const val DATABASE_NAME = "lenses_database"
    }
}