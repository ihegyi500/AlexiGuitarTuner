package com.ihegyi.alexiguitartuner.feat_metronome.di

import com.ihegyi.alexiguitartuner.feat_metronome.data.MetronomeRepositoryImpl
import com.ihegyi.alexiguitartuner.feat_metronome.domain.MetronomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MetronomeModule {

    @Provides
    @Singleton
    fun provideMetronomeRepository() : MetronomeRepository {
        return MetronomeRepositoryImpl()
    }

}