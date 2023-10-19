package com.ihegyi.alexiguitartuner.feat_tuner.data

import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.feat_tuner.domain.ButtonGenerationRepository
import javax.inject.Inject

class ButtonGenerationRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : ButtonGenerationRepository {
    override suspend fun getPitchesOfLastTuning(): List<Pitch> = database.pitchDAO.getPitchesByLastTuning()
    override suspend fun getTuningNameBySettings() = database.tuningDAO.getTuningNameBySettings()
}