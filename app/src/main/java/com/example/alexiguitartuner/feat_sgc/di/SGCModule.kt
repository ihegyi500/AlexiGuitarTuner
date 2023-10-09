package com.example.alexiguitartuner.feat_sgc.di

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.feat_sgc.data.SGCRepositoryImpl
import com.example.alexiguitartuner.feat_sgc.domain.SGCRepository
import com.example.alexiguitartuner.feat_sgc.domain.usecase.CalculateStringGaugeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SGCModule {

    @Provides
    @Singleton
    fun provideDispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideCSGCRepository(appDatabase: AppDatabase) : SGCRepository {
        return SGCRepositoryImpl(appDatabase)
    }

    @Provides
    @Singleton
    fun provideCalculateStringGaugeUseCase() : CalculateStringGaugeUseCase {
        return CalculateStringGaugeUseCase()
    }
}