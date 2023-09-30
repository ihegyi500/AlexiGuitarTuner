package com.example.alexiguitartuner.feat_tuner.domain

interface IPitchDetectionRepository {
    fun getPitchName(frequency: Double): String

}