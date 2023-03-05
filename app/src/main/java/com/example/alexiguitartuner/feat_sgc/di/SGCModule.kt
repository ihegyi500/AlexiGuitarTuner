package com.example.alexiguitartuner.feat_sgc.di

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.feat_sgc.data.SGCRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SGCModule {

    @Provides
    @Singleton
    fun provideCSGCRepository(appDatabase: AppDatabase) : SGCRepository {
        return SGCRepository(appDatabase)
    }
}