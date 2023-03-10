package com.example.alexiguitartuner.commons.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.alexiguitartuner.commons.domain.*
import com.example.alexiguitartuner.commons.domain.converter.Converters

@TypeConverters(Converters::class)
@Database(
    entities = [Pitch::class,
        Chord::class,
        PitchChordCrossRef::class,
        ChordTable::class,
        InstrumentString::class,
        Tuning::class,
        PitchTuningCrossRef::class,
        Instrument::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val chordDAO : ChordDAO
    abstract val instrumentDAO : InstrumentDAO
    abstract val tuningDAO : TuningDAO
    abstract val stringDAO : StringDAO
    abstract val pitchDAO : PitchDAO


    companion object {
        const val DATABASE_NAME = "alexiGuitarTuner_db"
    }
}