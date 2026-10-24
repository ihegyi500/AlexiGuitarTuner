package com.ihegyi.alexiguitartuner.compose

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.ihegyi.alexiguitartuner.commons.domain.entities.*
import com.ihegyi.alexiguitartuner.commons.presentation.ui.AlexiTheme
import com.ihegyi.alexiguitartuner.feat_chordtable.presentation.state.ChordTableUIState
import com.ihegyi.alexiguitartuner.feat_chordtable.presentation.ui.ChordTableScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChordTableScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun selectsInstrumentTuningChordAndChangesPosition() {
        val guitar = Instrument(1, "Guitar", 6)
        val standard = Tuning(1, "Standard", 1)
        val chord = Chord(1, "C major", 1)
        val tables = listOf(ChordTable(1, 1, 0, listOf(0, 1, 0, 2, 3, -1)), ChordTable(2, 1, 1, listOf(3, 5, 5, 5, 3, -1)))
        var state by mutableStateOf(ChordTableUIState(listOf(guitar), guitar, listOf(standard), standard,
            listOf(chord), chord, tables, tables[0], 0, listOf(Pitch(440.0, "A4"))))
        var selected = ""
        compose.setContent { AlexiTheme {
            ChordTableScreen(state, { selected = "instrument" }, { selected = "tuning" }, { selected = "chord" },
                { state = state.copy(selectedPosition = 0, selectedChordTable = tables[0]) },
                { state = state.copy(selectedPosition = 1, selectedChordTable = tables[1]) })
        } }
        listOf("chord_instrument" to "Guitar", "chord_tuning" to "Standard", "chord_choice" to "C major").forEach { (tag, label) ->
            compose.onNodeWithTag(tag).performScrollTo().performClick()
            compose.onAllNodesWithText(label).onLast().performClick()
        }
        compose.runOnIdle { assertEquals("chord", selected) }
        compose.onNodeWithTag("next_position").performScrollTo().performClick()
        compose.onNodeWithTag("chord_position").assertTextEquals("Position: 2")
        compose.onNodeWithTag("next_position").assertIsNotEnabled()
        compose.onNodeWithTag("previous_position").performClick()
        compose.onNodeWithTag("chord_position").assertTextEquals("Position: 1")
        compose.onNodeWithTag("chord_diagram").assertExists()
    }

    @Test fun emptyDataDisablesSelectorsAndExplainsMissingPositions() {
        compose.setContent { AlexiTheme { ChordTableScreen(ChordTableUIState.initial_state, {}, {}, {}, {}, {}) } }
        compose.onNodeWithTag("chord_choice").assertIsNotEnabled()
        compose.onNodeWithText("No chord positions for this tuning.").assertExists()
    }
}
