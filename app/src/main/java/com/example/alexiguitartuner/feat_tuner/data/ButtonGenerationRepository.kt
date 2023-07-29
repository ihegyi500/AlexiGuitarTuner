package com.example.alexiguitartuner.feat_tuner.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.domain.Pitch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ButtonGenerationRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getPitchesOfLastTuning(): Flow<List<Pitch>> = database.pitchDAO.getPitchesOfLastTuning()
}