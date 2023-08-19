package com.example.alexiguitartuner.feat_sgc.domain

import com.example.alexiguitartuner.commons.domain.InstrumentString
import kotlin.math.pow

class CalculateStringGaugeUseCase {

    companion object {
        private const val GRAVITY_ACCELERATION = 386.4
        private const val REGRESSION_FACTOR = 2.1242
    }

    operator fun invoke(instrumentString: InstrumentString) : String {
        return if(instrumentString.frequency == 0.0)
            "Pitch not found!"
        else {
            val unitWeight = calculateUnitWeight(instrumentString)
            String.format("%.4f", calculateStringGauge(unitWeight)) + " in"
        }
    }

    private fun calculateUnitWeight(instrumentString: InstrumentString) : Double =
        (instrumentString.tension.toString().toDouble() * GRAVITY_ACCELERATION) /
        ((2 * instrumentString.frequency * instrumentString.scaleLength.toString().toDouble()).pow(2))

    private fun calculateStringGauge(unitWeight : Double) : Double =
        ( REGRESSION_FACTOR *(unitWeight.pow(0.5)))
}