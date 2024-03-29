package com.ihegyi.alexiguitartuner.feat_sgc.di

import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.feat_sgc.data.SGCRepositoryImpl
import com.ihegyi.alexiguitartuner.feat_sgc.domain.SGCRepository
import com.ihegyi.alexiguitartuner.feat_sgc.domain.usecase.CalculateStringGaugeUseCase
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
        return SGCRepositoryImpl(appDatabase)
    }

    @Provides
    @Singleton
    fun provideCalculateStringGaugeUseCase() : CalculateStringGaugeUseCase {
        return CalculateStringGaugeUseCase()
    }
}