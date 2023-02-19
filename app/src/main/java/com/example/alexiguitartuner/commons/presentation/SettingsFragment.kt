package com.example.alexiguitartuner.commons.presentation

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.alexiguitartuner.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}