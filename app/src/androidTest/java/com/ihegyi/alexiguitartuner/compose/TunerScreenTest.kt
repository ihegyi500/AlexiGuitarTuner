package com.ihegyi.alexiguitartuner.compose

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.presentation.ui.AlexiTheme
import com.ihegyi.alexiguitartuner.feat_tuner.presentation.state.PitchButtonsUIState
import com.ihegyi.alexiguitartuner.feat_tuner.presentation.ui.TunerScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TunerScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun displaysPitchAndPlaysAndStopsReferenceTone() {
        var frequency = 0.0
        var stopped = false
        compose.setContent { AlexiTheme {
            TunerScreen(Pitch(440.0, "A4"), PitchButtonsUIState("Standard", listOf(Pitch(82.41, "E2"))),
                true, {}, { frequency = it }, { stopped = true })
        } }
        compose.onNodeWithTag("detected_pitch").assertTextEquals("A4")
        compose.onNodeWithTag("detected_frequency").assertTextContains("440", substring = true)
        compose.onNodeWithTag("pitch_0").performClick()
        compose.runOnIdle { assertEquals(82.41, frequency, 0.001) }
        compose.onNodeWithTag("stop_pitch").performClick()
        compose.runOnIdle { assertEquals(true, stopped) }
    }

    @Test fun deniedPermissionOffersRetryAndKeepsReferenceTones() {
        var requested = false
        compose.setContent { AlexiTheme {
            TunerScreen(Pitch(0.0, ""), PitchButtonsUIState("Standard", listOf(Pitch(440.0, "A4"))),
                false, { requested = true }, {}, {})
        } }
        compose.onNodeWithTag("grant_microphone").performClick()
        compose.onNodeWithTag("pitch_0").assertIsEnabled()
        compose.runOnIdle { assertEquals(true, requested) }
    }
}
