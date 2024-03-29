package com.ihegyi.alexiguitartuner.commons.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ihegyi.alexiguitartuner.commons.data.db.dao.ChordDAO
import com.ihegyi.alexiguitartuner.commons.data.db.dao.ChordTableDAO
import com.ihegyi.alexiguitartuner.commons.data.db.dao.InstrumentDAO
import com.ihegyi.alexiguitartuner.commons.data.db.dao.PitchDAO
import com.ihegyi.alexiguitartuner.commons.data.db.dao.PitchTuningCrossRefDAO
import com.ihegyi.alexiguitartuner.commons.data.db.dao.StringDAO
import com.ihegyi.alexiguitartuner.commons.data.db.dao.TuningDAO
import com.ihegyi.alexiguitartuner.commons.data.db.dao.UserSettingsDAO
import com.ihegyi.alexiguitartuner.commons.domain.*
import com.ihegyi.alexiguitartuner.commons.domain.converter.Converters
import com.ihegyi.alexiguitartuner.commons.domain.entities.Chord
import com.ihegyi.alexiguitartuner.commons.domain.entities.ChordTable
import com.ihegyi.alexiguitartuner.commons.domain.entities.Instrument
import com.ihegyi.alexiguitartuner.commons.domain.entities.InstrumentString
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import com.ihegyi.alexiguitartuner.commons.domain.entities.UserSettings

@TypeConverters(Converters::class)
@Database(
    entities = [Pitch::class,
        Chord::class,
        ChordTable::class,
        InstrumentString::class,
        Tuning::class,
        Instrument::class,
        UserSettings::class,
        PitchTuningCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val chordDAO : ChordDAO
    abstract val chordTableDAO : ChordTableDAO
    abstract val instrumentDAO : InstrumentDAO
    abstract val tuningDAO : TuningDAO
    abstract val stringDAO : StringDAO
    abstract val pitchDAO : PitchDAO
    abstract val pitchTuningCrossRefDAO : PitchTuningCrossRefDAO
    abstract val userSettingsDAO : UserSettingsDAO

    companion object {
        const val DATABASE_NAME = "alexiGuitarTuner_db"
    }
}