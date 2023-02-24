package com.example.alexiguitartuner.feat_tuner.data

import com.example.alexiguitartuner.commons.domain.Pitch
import kotlin.math.pow

class TunerDataSource() {

    companion object {
        const val PITCH_DIFF = 0.4F
        const val FAULT_DIFF = 0.05F
        const val MAX_OCTAVE_NUMBER = 8
    }

    var concertPitch : Pitch = Pitch("A4",440.0)
    var pitchList : MutableList<Pitch> = mutableListOf()

    fun initializePitchList(){
        val pitchNames = arrayOf("C","C#","D","D#","E","F","F#","G","G#","A","A#","B")
        val octaves = IntRange(0, MAX_OCTAVE_NUMBER)
        val concertPitchIndex = pitchNames.indexOf("A") + pitchNames.size * octaves.indexOf(4)
        var index = 0
        octaves.forEach { octave ->
            pitchNames.forEach { pitch ->
                if(index != concertPitchIndex)
                    pitchList.add(Pitch("$pitch$octave",(concertPitch.frequency * 2.0.pow(1.0/12).pow(index - concertPitchIndex))))
                else
                    pitchList.add(concertPitchIndex, concertPitch)
                index++
            }
        }
    }

    /*fun replaceHalfNote() {
        var symbol = "#"
        for(pitch in pitchList) {
            if(pitch.name.contains('b')) {
                symbol =
            }
        }
    }*/

    /*
    fun binarySearchRecursive(input: IntArray, eleToSearch: Int, low:Int, high:Int): Int {

        while(low <=high) {
            val mid = (low + high) /2
            when {
                eleToSearch > input[mid] -> return binarySearchRecursive(input, eleToSearch, mid+1, high)   // element is greater than middle element of array, so it will be in right half. Recursion will call the right half again
                eleToSearch < input[mid] -> return binarySearchRecursive(input, eleToSearch, low, mid-1)    //element is less than middle element of array, so it will be in left half of the array. Recursion will call the left half again.
                eleToSearch == input[mid] -> return mid // element found.
            }
        }
        return -1
    }*/

}