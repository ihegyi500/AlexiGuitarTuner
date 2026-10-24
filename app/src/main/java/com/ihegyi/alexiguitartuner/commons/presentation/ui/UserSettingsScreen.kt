package com.ihegyi.alexiguitartuner.commons.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ihegyi.alexiguitartuner.R.string
import com.ihegyi.alexiguitartuner.commons.domain.entities.UserSettings
import com.ihegyi.alexiguitartuner.commons.presentation.viewmodel.*

@Composable
fun UserSettingsRoute(viewModel: UserSettingsViewModel = hiltViewModel()) {
    val state by viewModel.userSettings.collectAsStateWithLifecycle()
    val owner = LocalLifecycleOwner.current
    var dialog by rememberSaveable { mutableStateOf("") }
    var changed by rememberSaveable { mutableStateOf(false) }
    DisposableEffect(owner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE && changed) {
                viewModel.updateUserSettings()
                changed = false
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
            if (changed) viewModel.updateUserSettings()
        }
    }
    UserSettingsScreen(state, { viewModel.updateUseSharp(it); changed = true }, { viewModel.updateUseEnglish(it); changed = true },
        { dialog = "select" }, { dialog = "create" }, { dialog = "delete" })
    when (dialog) {
        "select" -> SelectTuningSheet(hiltViewModel<SelectTuningViewModel>()) { dialog = "" }
        "create" -> CreateTuningSheet(hiltViewModel<CreateTuningViewModel>()) { dialog = "" }
        "delete" -> DeleteTuningsConfirmation(
            onConfirm = { viewModel.deleteAllCustomTunings(); dialog = "" }, onDismiss = { dialog = "" })
    }
}

@Composable
fun UserSettingsScreen(state: UserSettings, onSharp: (Boolean) -> Unit, onEnglish: (Boolean) -> Unit,
    onSelect: () -> Unit, onCreate: () -> Unit, onDelete: () -> Unit) {
    val halfNoteLabel = stringResource(string.half_note_display)
    val notationLabel = stringResource(string.notation_system)
    ScreenColumn("settings_page") {
        Text(stringResource(string.title_settings), style = MaterialTheme.typography.headlineMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(string.half_note_display), Modifier.weight(1f))
            Text(stringResource(if (state.useSharp) string.sharp else string.flat_b))
            Switch(state.useSharp, onSharp, Modifier.testTag("sharp_switch").semantics { contentDescription = halfNoteLabel })
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(string.notation_system), Modifier.weight(1f))
            Text(stringResource(if (state.useEnglish) string.english else string.german))
            Switch(state.useEnglish, onEnglish, Modifier.testTag("english_switch").semantics { contentDescription = notationLabel })
        }
        Text(stringResource(string.select_default_tuning_for_the_tuner))
        Button(onClick = onSelect, modifier = Modifier.fillMaxWidth().testTag("open_select_tuning")) {
            Text(stringResource(string.select_tuning_settings))
        }
        OutlinedButton(onClick = onCreate, modifier = Modifier.fillMaxWidth().testTag("open_create_tuning")) {
            Text(stringResource(string.create_custom_tuning))
        }
        OutlinedButton(onClick = onDelete, modifier = Modifier.fillMaxWidth().testTag("open_delete_tunings")) {
            Text(stringResource(string.delete_all_custom_tunings))
        }
    }
}
