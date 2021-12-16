package ru.lenses.lensestoday.repository

import ru.lenses.lensestoday.room.Lenses
import ru.lenses.lensestoday.room.LensesDao
import javax.inject.Inject

class LensesRepository @Inject constructor(private val lensesDao: LensesDao) {

    fun readLenses() = lensesDao.readLenses()

    suspend fun addLenses(lenses: Lenses) = lensesDao.addLenses(lenses)

    suspend fun updateLenses(lenses: Lenses) = lensesDao.updateLenses(lenses)

    suspend fun deleteLenses(lenses: Lenses) = lensesDao.deleteLenses(lenses)

    suspend fun deleteAllLenses() = lensesDao.deleteAllLenses()
}