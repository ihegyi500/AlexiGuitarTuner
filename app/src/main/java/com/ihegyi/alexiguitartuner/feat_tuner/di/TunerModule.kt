package com.ihegyi.alexiguitartuner.feat_tuner.di

import be.tarsos.dsp.io.android.AudioDispatcherFactory.*
import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.feat_tuner.data.ButtonGenerationRepositoryImpl
import com.ihegyi.alexiguitartuner.feat_tuner.data.PitchDetectionRepositoryImpl
import com.ihegyi.alexiguitartuner.feat_tuner.data.PitchGenerationRepositoryImpl
import com.ihegyi.alexiguitartuner.feat_tuner.domain.ButtonGenerationRepository
import com.ihegyi.alexiguitartuner.feat_tuner.domain.PitchDetectionRepository
import com.ihegyi.alexiguitartuner.feat_tuner.domain.PitchGenerationRepository
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