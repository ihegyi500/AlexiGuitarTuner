package com.ihegyi.alexiguitartuner.commons.di

import android.app.Application
import androidx.room.Room
import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration()
         .createFromAsset("alexiGuitarTuner_db.db")
         //.setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
         .build()
    }
}