package com.example.alexiguitartuner.feat_chordtable.di

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.feat_chordtable.data.ChordTableRepositoryImpl
import com.example.alexiguitartuner.feat_chordtable.domain.ChordTableRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChordTableModule {

    @Provides
    @Singleton
    fun provideChordTableRepository(
        appDatabase: AppDatabase
    ) : ChordTableRepository {
        return ChordTableRepositoryImpl(appDatabase)
    }


}