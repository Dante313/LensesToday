package ru.lenses.lensestoday.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.lenses.lensestoday.room.LensesDatabase
import ru.lenses.lensestoday.room.LensesDatabase.Companion.DATABASE_NAME
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideLensesDb(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        LensesDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideLensesDao(lensesDatabase: LensesDatabase) = lensesDatabase.lensesDao()
}