package com.example.alexiguitartuner.feat_metronome.data

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import com.example.alexiguitartuner.feat_metronome.domain.Rhythm
import com.example.alexiguitartuner.feat_metronome.domain.Tone
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.system.measureTimeMillis

class MetronomeRepository() {
    
    companion object {
        private const val TONE_LENGTH = 50
        const val MIN_NOTE = 3
        private const val MAX_NOTE = 8
        const val INIT_BPM = 40
        private const val MIN_IN_MILLIS = 60000
    }

    private var toneGenerator : ToneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    private var metronomePlayerJob : Job? = null
    private var isPlaying : Boolean = false

    private var _rhythmList = MutableStateFlow(MutableList(MIN_NOTE) { Tone.LOUD })
    val rhythmList : StateFlow<MutableList<Tone>> = _rhythmList.asStateFlow()

    private val _rhythmListIterator = MutableStateFlow(0)
    val rhythmListIterator : StateFlow<Int> = _rhythmListIterator.asStateFlow()

    private var tempo : Long = 0
    private var rhythm : Rhythm = Rhythm.QUARTER

    private val _bpm = MutableStateFlow(INIT_BPM)
    val bpm : StateFlow<Int> = _bpm.asStateFlow()

    fun setBPM(value : Int) {
        _bpm.value = value
        tempo = (MIN_IN_MILLIS / (bpm.value * rhythm.value)).toLong()
    }

    fun getRhythm() : Rhythm = rhythm

    fun setRhythm() {
        rhythm = rhythm.getNextRhythm()
        setBPM(bpm.value)
    }

    fun playMetronome(){
        Log.d("Metronome", "${rhythmList.value[rhythmListIterator.value].value}")
        if (!isPlaying) {
            isPlaying = true
            metronomePlayerJob = GlobalScope.launch(Dispatchers.Default) {
                while (isPlaying) {
                    val time = measureTimeMillis {
                        toneGenerator.startTone(rhythmList.value[rhythmListIterator.value].value, TONE_LENGTH)
                        toneGenerator.stopTone()
                        Log.d("try", "Metronome is running: ${rhythmListIterator.value}")
                        if (rhythmListIterator.value == (rhythmList.value.size - 1))
                            _rhythmListIterator.value = 0
                        else
                            _rhythmListIterator.value++
                    }
                    delay(tempo - time)
                }
            }
        }
    }

    fun pauseMetronome(){
        if (isPlaying) {
            if (metronomePlayerJob != null) metronomePlayerJob?.cancel()
            isPlaying = false
            _rhythmListIterator.value = 0
        }
    }

    fun insertNote() {
        if(rhythmList.value.size < MAX_NOTE) {
            rhythmList.value.add(Tone.LOUD)
        } else {
            throw Exception("Only $MAX_NOTE notes are allowed!")
        }
    }

    fun removeNote() {
        if(rhythmList.value.size > MIN_NOTE) {
            rhythmList.value.removeLast()
        } else {
            throw Exception("At least $MIN_NOTE notes are necessary!")
        }
    }

    fun setToneByIndex(index : Int) {
        _rhythmList.value[index] = rhythmList.value[index].getNextTone()
    }

    fun getIsPlaying() = isPlaying

}