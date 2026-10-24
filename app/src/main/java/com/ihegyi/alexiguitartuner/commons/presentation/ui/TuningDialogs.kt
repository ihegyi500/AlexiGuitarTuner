package com.ihegyi.alexiguitartuner.commons.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ihegyi.alexiguitartuner.R.string
import com.ihegyi.alexiguitartuner.commons.presentation.viewmodel.CreateTuningViewModel
import com.ihegyi.alexiguitartuner.commons.presentation.viewmodel.SelectTuningViewModel
import kotlinx.coroutines.launch

@Composable
fun SelectTuningSheet(viewModel: SelectTuningViewModel, onDismiss: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel) { viewModel.initTuningList() }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(string.select_tuning)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ChoiceField(stringResource(string.select_instrument), state.selectedInstrument?.name.orEmpty(),
                    state.instrumentList.map { it.name }, "select_instrument") { index ->
                    scope.launch { viewModel.selectInstrument(state.instrumentList[index]) }
                }
                ChoiceField(stringResource(string.select_tuning), state.selectedTuning?.name.orEmpty(),
                    state.tuningList.map { it.name }, "select_tuning") { index ->
                    scope.launch { viewModel.selectTuning(state.tuningList[index]) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { viewModel.updateSelectedTuning(); onDismiss() }, enabled = state.selectedTuning != null) {
                Text(stringResource(string.ok))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(string.cancel)) } }
    )
}

@Composable
fun CreateTuningSheet(viewModel: CreateTuningViewModel, onDismiss: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var name by rememberSaveable { mutableStateOf("") }
    var revision by remember { mutableIntStateOf(0) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(string.create_custom_tuning)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it },
                    label = { Text(stringResource(string.tuning_name)) }, modifier = Modifier.testTag("tuning_name"))
                ChoiceField(stringResource(string.select_instrument), state.selectedInstrument?.name.orEmpty(),
                    state.instrumentList.map { it.name }, "create_instrument") { index ->
                    viewModel.changeSelectedInstrument(index)
                    revision++
                }
                key(revision, state.selectedInstrument) {
                    if (state.selectedInstrument != null) {
                        val pitches = viewModel.pitchList
                        state.pitchesOfTuning.toList().forEachIndexed { index, pitch ->
                            ChoiceField(stringResource(string.string_number, index + 1), pitch.name,
                                pitches.map { it.name }, "tuning_pitch_$index") { selected ->
                                viewModel.updatePitchInList(index, selected)
                                revision++
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { viewModel.insertTuning(name.trim()); onDismiss() },
                enabled = name.isNotBlank() && state.selectedInstrument != null) { Text(stringResource(string.ok)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(string.cancel)) } }
    )
}

@Composable
fun DeleteTuningsConfirmation(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text(stringResource(string.delete_all_custom_tunings)) },
        text = { Text(stringResource(string.delete_tunings_warning)) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(string.delete)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(string.cancel)) } })
}
