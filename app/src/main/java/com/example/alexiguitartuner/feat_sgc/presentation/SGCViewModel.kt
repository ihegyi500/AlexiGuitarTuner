package com.example.alexiguitartuner.feat_sgc.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.commons.domain.InstrumentString
import com.example.alexiguitartuner.feat_sgc.data.SGCRepository
import com.example.alexiguitartuner.feat_sgc.domain.CalculateStringGaugeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SGCViewModel @Inject constructor(
    private val stringGaugeUseCase: CalculateStringGaugeUseCase,
    private val sgcRepository: SGCRepository
): ViewModel() {

    private var getInstrumentStringJob: Job? = null

    private var _listOfStrings = MutableStateFlow<List<InstrumentString>>(emptyList())
    val listOfStrings: StateFlow<List<InstrumentString>> = _listOfStrings.asStateFlow()

    init {
        getInstrumentString()
    }

    fun insertString() {
        viewModelScope.launch(Dispatchers.IO) { sgcRepository.insertString() }
    }

    fun updateString(string: InstrumentString) {
        viewModelScope.launch(Dispatchers.IO) { sgcRepository.updateString(string) }
    }

    fun deleteString(string:InstrumentString) {
        viewModelScope.launch(Dispatchers.IO) { sgcRepository.deleteString(string) }
    }

    fun calculateStringGauge(string: InstrumentString) : String = stringGaugeUseCase(string)

    private fun getInstrumentString() {
        getInstrumentStringJob?.cancel()
        getInstrumentStringJob = sgcRepository.getInstrumentStrings()
            .onEach {
                _listOfStrings.value = it
            }
            .launchIn(viewModelScope)
    }

}