package com.example.alexiguitartuner.feat_metronome.di

import com.example.alexiguitartuner.feat_metronome.data.MetronomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MetronomeModule {

    @Provides
    @Singleton
    fun provideMetronomeRepository() : MetronomeRepository {
        return MetronomeRepository()
    }

}