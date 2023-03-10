package com.example.alexiguitartuner.feat_sgc.domain

import com.example.alexiguitartuner.commons.domain.InstrumentString
import com.example.alexiguitartuner.feat_sgc.data.SGCRepository
import javax.inject.Inject
import kotlin.math.pow

class CalculateStringGaugeUseCase(
    private val sgcRepository: SGCRepository
) {

    companion object {
        private const val GRAVITY_ACCELERATION = 386.4
    }

    suspend operator fun invoke(instrumentString: InstrumentString) : String {
        val freq = sgcRepository.getFrequencyOfPitch(instrumentString.name)
        if(freq == 0.0)
            return "Pitch not found!"
        else {
            val unitWeight = calculateUnitWeight(instrumentString, freq)
            return String.format("%.4f", calculateStringGauge(unitWeight)) + "in"
        }
    }

    private fun calculateUnitWeight(instrumentString: InstrumentString, freq : Double) : Double =
        (instrumentString.tension.toString().toDouble() * GRAVITY_ACCELERATION) /
        ((2 * freq * instrumentString.scaleLength.toString().toDouble()).pow(2))

    private fun calculateStringGauge(unitWeight : Double) : Double =
        (2.1242*(unitWeight.pow(0.5)))
}