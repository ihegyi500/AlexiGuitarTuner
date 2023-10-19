package com.ihegyi.alexiguitartuner.feat_sgc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihegyi.alexiguitartuner.commons.domain.entities.InstrumentString
import com.ihegyi.alexiguitartuner.feat_sgc.domain.SGCRepository
import com.ihegyi.alexiguitartuner.feat_sgc.domain.usecase.CalculateStringGaugeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SGCViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
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
        viewModelScope.launch(dispatcher) { sgcRepository.insertString() }
    }
    fun updateString(string: InstrumentString) {
        viewModelScope.launch(dispatcher) { sgcRepository.updateString(string) }
    }
    fun deleteString(string: InstrumentString) {
        viewModelScope.launch(dispatcher) { sgcRepository.deleteString(string) }
    }
    fun calculateStringGauge(string: InstrumentString) = stringGaugeUseCase(string)

}