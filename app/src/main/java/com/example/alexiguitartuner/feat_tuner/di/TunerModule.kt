package com.example.alexiguitartuner.feat_tuner.di

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.io.android.AudioDispatcherFactory.*
import com.example.alexiguitartuner.feat_tuner.data.PitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.data.PitchGenerationRepository
import com.example.alexiguitartuner.feat_tuner.data.TunerDataSource
import com.example.alexiguitartuner.feat_tuner.domain.FindPitchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TunerModule {

    @Provides
    @Singleton
    fun providePitchDetectionRepository() : PitchDetectionRepository {
        return PitchDetectionRepository()
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
    fun provideTunerDataSource() : TunerDataSource {
        return TunerDataSource()
    }

}