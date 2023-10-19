package com.ihegyi.alexiguitartuner.feat_metronome.presentation.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.ihegyi.alexiguitartuner.feat_metronome.data.service.MetronomeService
import com.ihegyi.alexiguitartuner.feat_metronome.domain.MetronomeRepository
import com.ihegyi.alexiguitartuner.feat_metronome.domain.MetronomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MetronomeViewModel @Inject constructor(
    private val metronomeRepository: MetronomeRepository
) : ViewModel() {

    fun startService(context: Context) {
        context.startService(
            Intent(context, MetronomeService::class.java
            ).also {
            it.action = MetronomeService.Actions.START.toString()
        })
    }

    fun stopService(context: Context) {
        context.startService(
            Intent(context, MetronomeService::class.java
            ).also {
            it.action = MetronomeService.Actions.STOP.toString()
        })
    }

    fun getMetronomeState() : StateFlow<MetronomeState> = metronomeRepository.metronomeState

    fun insertNote() {
        metronomeRepository.insertNote()
    }

    fun removeNote() {
        metronomeRepository.removeNote()
    }

    fun setBPM(bpm: Int) {
        metronomeRepository.setBPM(bpm)
    }

    fun setRhythm() {
        metronomeRepository.setRhythm()
    }

    fun setToneByIndex(index: Int) {  metronomeRepository.setToneByIndex(index) }

}