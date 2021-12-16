package ru.lenses.lensestoday.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.lenses.lensestoday.repository.LensesRepository
import ru.lenses.lensestoday.room.LensesDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideLensesRepository(lensesDao: LensesDao) = LensesRepository(lensesDao)
}