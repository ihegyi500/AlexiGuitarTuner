package com.ihegyi.alexiguitartuner.feat_chordtable.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ihegyi.alexiguitartuner.R.string
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.presentation.ui.*
import com.ihegyi.alexiguitartuner.feat_chordtable.presentation.state.ChordTableUIState
import com.ihegyi.alexiguitartuner.feat_chordtable.presentation.viewmodel.ChordTableViewModel
import kotlinx.coroutines.launch

@Composable
fun ChordTableRoute(viewModel: ChordTableViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    ChordTableScreen(state,
        { index -> scope.launch { viewModel.selectInstrument(state.instrumentList[index]) } },
        { index -> scope.launch { viewModel.selectTuning(state.tuningList[index]) } },
        { index -> scope.launch { viewModel.selectChord(state.chordList[index]) } },
        viewModel::decreaseChordTablePosition, viewModel::increaseChordTablePosition)
}

@Composable
fun ChordTableScreen(state: ChordTableUIState, onInstrument: (Int) -> Unit, onTuning: (Int) -> Unit,
    onChord: (Int) -> Unit, onPrevious: () -> Unit, onNext: () -> Unit) {
    ScreenColumn("chords_page") {
        Text(stringResource(string.title_chords), style = MaterialTheme.typography.headlineMedium)
        ChoiceField(stringResource(string.select_instrument), state.selectedInstrument?.name.orEmpty(),
            state.instrumentList.map { it.name }, "chord_instrument", onInstrument)
        ChoiceField(stringResource(string.select_tuning), state.selectedTuning?.name.orEmpty(),
            state.tuningList.map { it.name }, "chord_tuning", onTuning)
        ChoiceField(stringResource(string.select_chord), state.selectedChord?.name.orEmpty(),
            state.chordList.map { it.name }, "chord_choice", onChord)
        if (state.selectedChordTable == null) Text(stringResource(string.no_chord_positions))
        else {
            ChordDiagram(state.tuningPitches, state.selectedChordTable.pitchPos)
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onPrevious, enabled = state.selectedPosition > 0,
                    modifier = Modifier.weight(1f).testTag("previous_position")) { Text(stringResource(string.show_previous_position)) }
                Text(stringResource(string.position_value, state.selectedPosition + 1), Modifier.testTag("chord_position"))
                TextButton(onClick = onNext, enabled = state.selectedPosition < state.chordTableList.lastIndex,
                    modifier = Modifier.weight(1f).testTag("next_position")) { Text(stringResource(string.show_next_position)) }
            }
        }
    }
}

@Composable
fun ChordDiagram(pitches: List<Pitch>, positions: List<Int>) {
    val names = pitches.sortedByDescending { it.frequency }.map { it.name }
    val positive = positions.filter { it > 0 }
    val firstFret = if ((positive.maxOrNull() ?: 0) <= 5) 1 else positive.minOrNull() ?: 1
    val color = MaterialTheme.colorScheme.primary
    val description = stringResource(string.chord_diagram_description,
        names.mapIndexed { index, name -> "$name: ${positions.getOrNull(index) ?: -1}" }.joinToString(", "))
    Column {
        Text(names.joinToString("  "), style = MaterialTheme.typography.labelLarge)
        Canvas(Modifier.fillMaxWidth().height(220.dp).testTag("chord_diagram").semantics { contentDescription = description }) {
            if (positions.isNotEmpty()) {
                val left = size.width * 0.12f
                val top = 20.dp.toPx()
                val fretWidth = (size.width - left - 16.dp.toPx()) / 5
                val stringHeight = (size.height - 40.dp.toPx()) / (positions.size - 1).coerceAtLeast(1)
                repeat(6) { fret -> drawLine(color, Offset(left + fret * fretWidth, top),
                    Offset(left + fret * fretWidth, top + (positions.size - 1) * stringHeight), 2.dp.toPx()) }
                positions.forEachIndexed { index, fret ->
                    val vertical = top + index * stringHeight
                    drawLine(color, Offset(left, vertical), Offset(left + 5 * fretWidth, vertical), 1.dp.toPx())
                    val horizontal = if (fret <= 0) left else left + (fret - firstFret + 0.5f) * fretWidth
                    val radius = 6.dp.toPx()
                    when {
                        fret < 0 -> {
                            drawLine(color, Offset(horizontal - radius, vertical - radius), Offset(horizontal + radius, vertical + radius), 2.dp.toPx())
                            drawLine(color, Offset(horizontal - radius, vertical + radius), Offset(horizontal + radius, vertical - radius), 2.dp.toPx())
                        }
                        fret == 0 -> drawCircle(color, radius, Offset(horizontal, vertical), style = Stroke(2.dp.toPx()))
                        else -> drawCircle(color, radius, Offset(horizontal, vertical))
                    }
                }
            }
        }
        Text((firstFret until firstFret + 5).joinToString("          "), style = MaterialTheme.typography.labelLarge)
    }
}
