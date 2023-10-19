package com.ihegyi.alexiguitartuner.commons.di

import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.commons.data.UserSettingsRepositoryImpl
import com.ihegyi.alexiguitartuner.commons.domain.UserSettingsRepository
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