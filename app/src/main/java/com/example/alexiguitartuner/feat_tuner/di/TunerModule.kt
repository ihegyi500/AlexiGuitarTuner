package com.example.alexiguitartuner.feat_tuner.di

import be.tarsos.dsp.io.android.AudioDispatcherFactory.*
import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.feat_tuner.data.ButtonGenerationRepository
import com.example.alexiguitartuner.feat_tuner.data.PitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.data.PitchGenerationRepository
import com.example.alexiguitartuner.feat_tuner.data.TunerDataSource
import com.example.alexiguitartuner.feat_tuner.domain.FindPitchUseCase
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
        return PitchDetectionRepository(database)
    }

    @Provides
    @Singleton
    fun provideFindPitchUseCase(
        tunerDataSource : TunerDataSource
    ) : FindPitchUseCase {
        return FindPitchUseCase(tunerDataSource)
    }

    @Provides
    @Singleton
    fun providePitchGenerationRepository() : PitchGenerationRepository {
        return PitchGenerationRepository()
    }

    @Provides
    @Singleton
    fun provideButtonGenerationRepository(
        database:AppDatabase
    ) : ButtonGenerationRepository {
        return ButtonGenerationRepository(database)
    }

    @Provides
    @Singleton
    fun provideTunerDataSource() : TunerDataSource {
        return TunerDataSource()
    }

}