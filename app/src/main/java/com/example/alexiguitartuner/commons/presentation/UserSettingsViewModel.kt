package com.example.alexiguitartuner.commons.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.commons.data.db.UserSettingsRepository
import com.example.alexiguitartuner.commons.domain.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private var _userSettings = MutableStateFlow<UserSettings?>(null)
    val userSettings: StateFlow<UserSettings?> = _userSettings.asStateFlow()

    init {
        viewModelScope.launch {
            _userSettings.update {
                userSettingsRepository.getUserSettings()
            }
        }
    }

    fun updateUseSharp(useSharp: Boolean) {
        _userSettings.update {
            it?.copy(useSharp = useSharp)
        }
    }

    fun updateUseEnglish(useEnglish: Boolean) {
        _userSettings.update {
            it?.copy(useEnglish = useEnglish)
        }
    }

    fun updateUserSettings() {
        viewModelScope.launch {
            userSettingsRepository.setUserSettings(_userSettings.value!!)
        }
    }

}