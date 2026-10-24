package com.ihegyi.alexiguitartuner.feat_metronome.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ihegyi.alexiguitartuner.R.string
import com.ihegyi.alexiguitartuner.commons.presentation.ui.ScreenColumn
import com.ihegyi.alexiguitartuner.feat_metronome.domain.MetronomeState
import com.ihegyi.alexiguitartuner.feat_metronome.presentation.viewmodel.MetronomeViewModel
import kotlin.math.roundToInt

@Composable
fun MetronomeRoute(viewModel: MetronomeViewModel = hiltViewModel()) {
    val observed by viewModel.getMetronomeState().collectAsStateWithLifecycle()
    var revision by remember { mutableIntStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }
    val state = remember(observed, revision) { observed.copy(beatList = observed.beatList.toMutableList()) }
    val context = LocalContext.current
    fun change(action: () -> Unit) {
        runCatching(action).onFailure { error = it.message }
        revision++
    }
    MetronomeScreen(state, viewModel::setBPM, { change(viewModel::insertNote) }, { change(viewModel::removeNote) },
        { index -> change { viewModel.setToneByIndex(index) } }, viewModel::setRhythm,
        { viewModel.startService(context) }, { viewModel.stopService(context) })
    error?.let { message ->
        AlertDialog(onDismissRequest = { error = null }, text = { Text(message) },
            confirmButton = { TextButton(onClick = { error = null }) { Text(stringResource(string.ok)) } })
    }
}

@Composable
fun MetronomeScreen(state: MetronomeState, onBpm: (Int) -> Unit, onAdd: () -> Unit, onRemove: () -> Unit,
    onBeat: (Int) -> Unit, onRhythm: () -> Unit, onStart: () -> Unit, onStop: () -> Unit) {
    ScreenColumn("metronome_page") {
        Text(stringResource(string.title_metronome), style = MaterialTheme.typography.headlineMedium)
        Text("${state.bpm} ${stringResource(string.bpm)}", style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.testTag("bpm_value"))
        Slider(state.bpm.toFloat().coerceIn(40f, 200f), { onBpm(it.roundToInt()) }, valueRange = 40f..200f,
            modifier = Modifier.testTag("bpm_slider"))
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            state.beatList.forEachIndexed { index, beat ->
                val current = state.isPlaying && index == (state.beatListIterator + state.beatList.size - 1) % state.beatList.size
                val description = stringResource(string.beat_description, index + 1, beat.name)
                FilterChip(selected = current, onClick = { onBeat(index) }, label = { Text("${index + 1}: ${beat.name}") },
                    modifier = Modifier.testTag("beat_$index").semantics { contentDescription = description })
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onRemove, modifier = Modifier.testTag("remove_beat")) { Text(stringResource(string.remove_note)) }
            OutlinedButton(onClick = onAdd, modifier = Modifier.testTag("add_beat")) { Text(stringResource(string.add_note)) }
        }
        OutlinedButton(onClick = onRhythm, modifier = Modifier.testTag("rhythm")) {
            Text(stringResource(string.rhythm_value, state.rhythm.name))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onStart, enabled = !state.isPlaying, modifier = Modifier.testTag("start_metronome")) { Text(stringResource(string.start)) }
            Button(onClick = onStop, enabled = state.isPlaying, modifier = Modifier.testTag("stop_metronome")) { Text(stringResource(string.stop)) }
        }
    }
}
