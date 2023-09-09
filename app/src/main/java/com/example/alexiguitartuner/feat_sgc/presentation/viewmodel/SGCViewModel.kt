package com.example.alexiguitartuner.feat_sgc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.commons.domain.entities.InstrumentString
import com.example.alexiguitartuner.feat_sgc.data.SGCRepository
import com.example.alexiguitartuner.feat_sgc.domain.usecase.CalculateStringGaugeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SGCViewModel @Inject constructor(
    private val stringGaugeUseCase: CalculateStringGaugeUseCase,
    private val sgcRepository: SGCRepository
): ViewModel() {
    val listOfStrings = sgcRepository.getInstrumentStrings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    suspend fun getPitch(frequency:  Double) = sgcRepository.getPitch(frequency)
    suspend fun getPitchByName(name: String) = sgcRepository.getPitchByName(name)
    fun insertString() {
        viewModelScope.launch(Dispatchers.IO) { sgcRepository.insertString() }
    }
    fun updateString(string: InstrumentString) {
        viewModelScope.launch(Dispatchers.IO) { sgcRepository.updateString(string) }
    }
    fun deleteString(string: InstrumentString) {
        viewModelScope.launch(Dispatchers.IO) { sgcRepository.deleteString(string) }
    }
    fun calculateStringGauge(string: InstrumentString) = stringGaugeUseCase(string)

}