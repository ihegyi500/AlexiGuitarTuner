package com.example.alexiguitartuner.feat_sgc.domain

import com.example.alexiguitartuner.commons.domain.InstrumentString

class CalculateStringGaugeUseCase() {
    operator fun invoke(instrumentString: InstrumentString) : String {
        return "${instrumentString.name}"
    }
}