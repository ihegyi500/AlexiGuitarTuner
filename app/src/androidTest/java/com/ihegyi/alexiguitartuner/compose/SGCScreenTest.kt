package com.ihegyi.alexiguitartuner.compose

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.ihegyi.alexiguitartuner.commons.domain.entities.InstrumentString
import com.ihegyi.alexiguitartuner.commons.presentation.ui.AlexiTheme
import com.ihegyi.alexiguitartuner.feat_sgc.presentation.ui.SGCScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SGCScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun addsEditsCalculatesAndDeletesString() {
        var strings by mutableStateOf(emptyList<InstrumentString>())
        var calculated: InstrumentString? = null
        compose.setContent { AlexiTheme {
            SGCScreen(strings, { strings = strings + InstrumentString(1, 82.41, 25.5, 16.0) },
                { strings = strings - it }, { "E2" }, { "0.042" }) { value, name -> calculated = value; name == "E2" }
        } }
        compose.onNodeWithTag("add_string").performClick()
        compose.onNodeWithTag("string_length_1").performScrollTo().performTextReplacement("26.5")
        compose.onNodeWithTag("calculate_1").performScrollTo().performClick()
        compose.runOnIdle { assertEquals(26.5, calculated!!.scaleLength, 0.01) }
        compose.onNodeWithTag("delete_string_1").performClick()
        compose.onNodeWithTag("string_1").assertDoesNotExist()
    }

    @Test fun invalidInputDoesNotSaveAndInfoCanBeDismissed() {
        var saved = false
        compose.setContent { AlexiTheme {
            SGCScreen(listOf(InstrumentString(1, 82.41, 25.5, 16.0)), {}, {}, { "E2" }, { "0.042" }) { _, _ -> saved = true; true }
        } }
        compose.onNodeWithTag("string_length_1").performScrollTo().performTextReplacement("-1")
        compose.onNodeWithTag("calculate_1").performScrollTo().performClick()
        compose.onNodeWithText("Enter a valid pitch name and positive scale length and tension.").assertExists()
        compose.runOnIdle { assertEquals(false, saved) }
        compose.onNodeWithTag("calculator_info").performScrollTo().performClick()
        compose.onNodeWithText("GOT IT").performClick()
        compose.onNodeWithText("GOT IT").assertDoesNotExist()
    }
}
