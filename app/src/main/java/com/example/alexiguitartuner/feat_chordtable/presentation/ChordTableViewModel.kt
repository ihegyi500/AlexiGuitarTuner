package com.example.alexiguitartuner.feat_chordtable.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.commons.domain.InstrumentWithTunings
import com.example.alexiguitartuner.feat_chordtable.data.ChordTableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChordTableViewModel @Inject constructor(
    private val chordTableRepository: ChordTableRepository
) : ViewModel() {

    private var _listOfInstruments = MutableStateFlow(emptyList<InstrumentWithTunings>())
    val listOfInstruments: StateFlow<List<InstrumentWithTunings>> = _listOfInstruments.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _listOfInstruments.value = chordTableRepository.getInstrumentsWithCords()
        }
    }

}