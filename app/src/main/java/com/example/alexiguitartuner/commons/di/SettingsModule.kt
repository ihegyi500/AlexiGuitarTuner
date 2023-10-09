package com.example.alexiguitartuner.commons.di

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.data.UserSettingsRepositoryImpl
import com.example.alexiguitartuner.commons.domain.UserSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {
    @Provides
    @Singleton
    fun provideSettingsRepository(
        appDatabase: AppDatabase
    ) : UserSettingsRepository {
        return UserSettingsRepositoryImpl(appDatabase)
    }
}