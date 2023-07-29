package com.example.alexiguitartuner.commons.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.alexiguitartuner.commons.data.db.dao.ChordDAO
import com.example.alexiguitartuner.commons.data.db.dao.InstrumentDAO
import com.example.alexiguitartuner.commons.data.db.dao.PitchDAO
import com.example.alexiguitartuner.commons.data.db.dao.StringDAO
import com.example.alexiguitartuner.commons.data.db.dao.TuningDAO
import com.example.alexiguitartuner.commons.data.db.dao.UserSettingsDAO
import com.example.alexiguitartuner.commons.domain.*
import com.example.alexiguitartuner.commons.domain.converter.Converters

@TypeConverters(Converters::class)
@Database(
    entities = [Pitch::class,
        Chord::class,
        ChordTable::class,
        InstrumentString::class,
        Tuning::class,
        Instrument::class,
        UserSettings::class,
        PitchChordCrossRef::class,
        PitchTuningCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val chordDAO : ChordDAO
    abstract val instrumentDAO : InstrumentDAO
    abstract val tuningDAO : TuningDAO
    abstract val stringDAO : StringDAO
    abstract val pitchDAO : PitchDAO
    abstract val userSettingsDAO : UserSettingsDAO

    companion object {
        const val DATABASE_NAME = "alexiGuitarTuner_db"
    }
}