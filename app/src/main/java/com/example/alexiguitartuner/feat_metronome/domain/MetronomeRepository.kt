package com.example.alexiguitartuner.feat_metronome.domain

import kotlinx.coroutines.flow.StateFlow

interface MetronomeRepository {
    val metronomeState : StateFlow<MetronomeState>
    suspend fun playMetronome()
    fun pauseMetronome()
    fun setRhythm()
    fun setBPM(value : Int)
    fun insertNote()
    fun removeNote()
    fun setToneByIndex(index : Int)
}