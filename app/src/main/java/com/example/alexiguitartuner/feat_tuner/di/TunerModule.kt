package com.example.alexiguitartuner.feat_tuner.di

import be.tarsos.dsp.io.android.AudioDispatcherFactory.*
import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.feat_tuner.data.ButtonGenerationRepositoryImpl
import com.example.alexiguitartuner.feat_tuner.data.PitchDetectionRepositoryImpl
import com.example.alexiguitartuner.feat_tuner.data.PitchGenerationRepositoryImpl
import com.example.alexiguitartuner.feat_tuner.domain.ButtonGenerationRepository
import com.example.alexiguitartuner.feat_tuner.domain.PitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.domain.PitchGenerationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TunerModule {

    @Provides
    @Singleton
    fun providePitchDetectionRepository(
        database:AppDatabase
    ) : PitchDetectionRepository {
        return PitchDetectionRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun providePitchGenerationRepository() : PitchGenerationRepository {
        return PitchGenerationRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideButtonGenerationRepository(
        database:AppDatabase
    ) : ButtonGenerationRepository {
        return ButtonGenerationRepositoryImpl(database)
    }

}