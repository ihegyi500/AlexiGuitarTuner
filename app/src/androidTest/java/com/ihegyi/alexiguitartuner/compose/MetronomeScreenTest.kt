package com.ihegyi.alexiguitartuner.compose

import androidx.compose.runtime.*
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.ihegyi.alexiguitartuner.commons.presentation.ui.AlexiTheme
import com.ihegyi.alexiguitartuner.feat_metronome.domain.*
import com.ihegyi.alexiguitartuner.feat_metronome.presentation.ui.MetronomeScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MetronomeScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun changesTempoBeatsAndRhythm() {
        var state by mutableStateOf(MetronomeState.initial_state.copy(beatList = MutableList(4) { Beat.LOUD }))
        compose.setContent { AlexiTheme {
            MetronomeScreen(state, { state = state.copy(bpm = it) },
                { state = state.copy(beatList = (state.beatList + Beat.LOUD).toMutableList()) },
                { state = state.copy(beatList = state.beatList.dropLast(1).toMutableList()) },
                { index -> state = state.copy(beatList = state.beatList.mapIndexed { position, beat ->
                    if (position == index) beat.getNextTone() else beat
                }.toMutableList()) }, { state = state.copy(rhythm = state.rhythm.getNextRhythm()) }, {}, {})
        } }
        compose.onNodeWithTag("bpm_slider").performSemanticsAction(SemanticsActions.SetProgress) { it(120f) }
        compose.onNodeWithTag("bpm_value").assertTextEquals("120 BPM")
        compose.onNodeWithTag("add_beat").performScrollTo().performClick()
        compose.onNodeWithTag("beat_4").assertExists()
        compose.onNodeWithTag("remove_beat").performClick()
        compose.onNodeWithTag("beat_4").assertDoesNotExist()
        compose.onNodeWithTag("beat_0").performClick()
        compose.runOnIdle { assertEquals(Beat.LOUDER, state.beatList[0]) }
        compose.onNodeWithTag("rhythm").performScrollTo().performClick()
        compose.onNodeWithTag("rhythm").assertTextContains("EIGHTH", substring = true)
    }

    @Test fun startAndStopReflectPlaybackState() {
        var state by mutableStateOf(MetronomeState.initial_state)
        compose.setContent { AlexiTheme {
            MetronomeScreen(state, {}, {}, {}, {}, {}, { state = state.copy(isPlaying = true) },
                { state = state.copy(isPlaying = false) })
        } }
        compose.onNodeWithTag("stop_metronome").performScrollTo().assertIsNotEnabled()
        compose.onNodeWithTag("start_metronome").performClick()
        compose.onNodeWithTag("start_metronome").assertIsNotEnabled()
        compose.onNodeWithTag("stop_metronome").performClick()
        compose.onNodeWithTag("start_metronome").assertIsEnabled()
    }
}
