package com.example.alexiguitartuner.feat_metronome.domain

interface IMetronomeRepository {
    fun setRhythm()
    fun setBPM(value : Int)
    fun insertNote()
    fun removeNote()
    fun setToneByIndex(index : Int)
}