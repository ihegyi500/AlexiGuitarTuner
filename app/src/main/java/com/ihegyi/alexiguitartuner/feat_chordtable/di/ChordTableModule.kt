package com.ihegyi.alexiguitartuner.feat_chordtable.di

import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.feat_chordtable.data.ChordTableRepositoryImpl
import com.ihegyi.alexiguitartuner.feat_chordtable.domain.ChordTableRepository
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