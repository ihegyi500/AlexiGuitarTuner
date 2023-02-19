package com.example.alexiguitartuner.feat_metronome.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.feat_metronome.data.MetronomeRepository
import com.example.alexiguitartuner.feat_metronome.data.MetronomeService
import com.example.alexiguitartuner.feat_metronome.domain.Rhythm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MetronomeViewModel @Inject constructor(
    private val metronomeRepository: MetronomeRepository
    ) : ViewModel() {

    private var _bpm = MutableStateFlow(MetronomeRepository.INIT_BPM)
    val bpm: MutableStateFlow<Int> = _bpm

    init {
        metronomeRepository.setBPM(bpm.value)
        viewModelScope.launch {
            metronomeRepository.bpm.collectLatest {
                _bpm.value = it
            }
        }
    }

    fun startService(context: Context) {
        context.startService(
            Intent(context,MetronomeService::class.java
            ).also {
            it.action = MetronomeService.START_SERVICE
        })
    }

    fun stopService(context: Context) {
        context.startService(
            Intent(context,MetronomeService::class.java
            ).also {
            it.action = MetronomeService.STOP_SERVICE
        })
    }

    fun insertNote() {
        metronomeRepository.insertNote()
    }

    fun removeNote() {
        metronomeRepository.removeNote()
    }

    fun setBPM(bpm: Int) {
        metronomeRepository.setBPM(bpm)
    }

    fun getRhythm(): Rhythm = metronomeRepository.getRhythm()

    fun setRhythm() {
        metronomeRepository.setRhythm()
    }


}