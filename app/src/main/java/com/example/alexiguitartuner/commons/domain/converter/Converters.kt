package com.example.alexiguitartuner.commons.domain.converter

import androidx.room.TypeConverter
import com.example.alexiguitartuner.feat_metronome.domain.Rhythm

class Converters {
    @TypeConverter
    fun fromListIntToString(intList: List<Int>): String = intList.toString()
    @TypeConverter
    fun toListIntFromString(stringList: String): List<Int> {
        val result = ArrayList<Int>()
        val split =stringList.replace("[","").replace("]","").replace(" ","").split(",")
        for (n in split) {
            try {
                result.add(n.toInt())
            } catch (e: Exception) {

            }
        }
        return result
    }
    @TypeConverter
    fun fromRhythmToInt(rhythm:Rhythm):Int = rhythm.ordinal

    @TypeConverter
    fun toRhythmFromInt(ordinal:Int):Rhythm = Rhythm.values()[ordinal]
}