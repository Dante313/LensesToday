package ru.lenses.lensestoday.room

import androidx.room.*

@Dao
interface LensesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLenses(lenses: Lenses)

    @Update
    suspend fun updateLenses(lenses: Lenses)

    @Delete
    suspend fun deleteLenses(lenses: Lenses)

    @Query("DELETE FROM lenses")
    suspend fun deleteAllLenses()

    @Query("SELECT * FROM lenses")
    fun readLenses(): Lenses
}