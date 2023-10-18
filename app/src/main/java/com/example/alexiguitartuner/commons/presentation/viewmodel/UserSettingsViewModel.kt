package com.example.alexiguitartuner.commons.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.commons.data.UserSettingsRepositoryImpl.Companion.initial_user_setting
import com.example.alexiguitartuner.commons.domain.UserSettingsRepository
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private var _userSettings = MutableStateFlow(initial_user_setting)
    val userSettings: StateFlow<UserSettings> = _userSettings.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            userSettingsRepository.getUserSettings().collectLatest { userSettings ->
                _userSettings.update {
                    userSettings
                }
            }
        }
    }

    fun updateUseSharp(useSharp: Boolean) {
        _userSettings.update {
            it.copy(useSharp = useSharp)
        }
    }

    fun updateUseEnglish(useEnglish: Boolean) {
        _userSettings.update {
            it.copy(useEnglish = useEnglish)
        }
    }

    fun deleteAllCustomTunings() {
        viewModelScope.launch(dispatcher) {
            val updatedUserSettings = _userSettings.value.copy(
                tuningId = 1
            )
            userSettingsRepository.updateUserSettings(updatedUserSettings)
            userSettingsRepository.deleteAllCustomTunings()
        }
    }

    fun updateUserSettings() {
        viewModelScope.launch(dispatcher) {
            userSettingsRepository.getUserSettings().collectLatest { oldUserSettings ->
                if(oldUserSettings.useSharp != _userSettings.value.useSharp
                    || oldUserSettings.useEnglish != _userSettings.value.useEnglish
                ) {
                    val pitchList = userSettingsRepository.getPitches()
                    when (Pair(_userSettings.value.useSharp, _userSettings.value.useEnglish)) {
                        Pair(true, true) -> {
                            updatePitchList(pitchList, Pair("B", "A#"))
                        }
                        Pair(true, false) -> {
                            updatePitchList(pitchList, Pair("H", "A#"))
                        }
                        Pair(false, true) -> {
                            updatePitchList(pitchList, Pair("B", "Bb"))
                        }
                        else -> {
                            updatePitchList(pitchList, Pair("H", "B"))
                        }
                    }
                    userSettingsRepository.updatePitches(pitchList)
                    val userSettings = oldUserSettings.copy(
                        useSharp = _userSettings.value.useSharp,
                        useEnglish = _userSettings.value.useEnglish
                    )
                    userSettingsRepository.updateUserSettings(userSettings)
                }
            }
        }
    }

    private fun updatePitchList(pitchList : List<Pitch>, notationSystemPitches : Pair<String, String>) {
        for (i in 1 until pitchList.size) {
            if (_userSettings.value.useSharp && pitchList[i].name.contains('b')) {
                pitchList[i].name = pitchList[i].name
                    .replace('b', '#')
                    .replace(pitchList[i].name.first(), pitchList[i - 1].name.first())
            } else if (!(_userSettings.value.useSharp) && pitchList[i].name.contains('#')) {
                pitchList[i].name = pitchList[i].name
                    .replace('#', 'b')
                    .replace(pitchList[i].name.first(), pitchList[i + 1].name.first())
            }
        }
        for (i in 11 until pitchList.size step 12) {
            pitchList[i].name =  notationSystemPitches.first + pitchList[i].name.last()
            pitchList[i - 1].name =  notationSystemPitches.second + pitchList[i - 1].name.last()
        }
    }


}