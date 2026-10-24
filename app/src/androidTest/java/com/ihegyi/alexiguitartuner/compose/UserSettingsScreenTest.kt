package com.ihegyi.alexiguitartuner.compose

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.ihegyi.alexiguitartuner.commons.domain.entities.UserSettings
import com.ihegyi.alexiguitartuner.commons.presentation.ui.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class UserSettingsScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun changesNotationAndOpensTuningActions() {
        var state by mutableStateOf(UserSettings(1, 1, true, true))
        var action = ""
        compose.setContent { AlexiTheme {
            UserSettingsScreen(state, { state = state.copy(useSharp = it) }, { state = state.copy(useEnglish = it) },
                { action = "select" }, { action = "create" }, { action = "delete" })
        } }
        compose.onNodeWithTag("sharp_switch").performClick().assertIsOff()
        compose.onNodeWithTag("english_switch").performClick().assertIsOff()
        listOf("open_select_tuning" to "select", "open_create_tuning" to "create", "open_delete_tunings" to "delete").forEach { (tag, expected) ->
            compose.onNodeWithTag(tag).performScrollTo().performClick()
            compose.runOnIdle { assertEquals(expected, action) }
        }
    }

    @Test fun deletingTuningsRequiresConfirmationAndCanBeCancelled() {
        var confirmed = false
        var open by mutableStateOf(true)
        compose.setContent { AlexiTheme { if (open) DeleteTuningsConfirmation({ confirmed = true; open = false }, { open = false }) } }
        compose.onNodeWithText("Cancel").performClick()
        compose.runOnIdle { assertEquals(false, confirmed); open = true }
        compose.onNodeWithText("Delete").performClick()
        compose.runOnIdle { assertEquals(true, confirmed) }
    }
}
