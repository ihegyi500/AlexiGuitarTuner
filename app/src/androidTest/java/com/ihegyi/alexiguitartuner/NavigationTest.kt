package com.ihegyi.alexiguitartuner

import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.room.Room
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.core.app.ApplicationProvider
import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.commons.di.DatabaseModule
import dagger.hilt.android.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class NavigationTest {
    @get:Rule(order = 0) val hilt = HiltAndroidRule(this)
    @get:Rule(order = 1) val compose = createAndroidComposeRule<MainActivity>()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    @BindValue @JvmField val database: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "compose-navigation-test.db")
        .createFromAsset("alexiGuitarTuner_db.db").build()

    @After fun closeDatabase() {
        compose.activityRule.scenario.close()
        database.close()
        context.deleteDatabase("compose-navigation-test.db")
    }

    private fun awaitHome() {
        compose.waitUntil(10_000) { compose.onAllNodesWithTag("nav_tuner").fetchSemanticsNodes().isNotEmpty() }
    }

    @Test fun bottomNavigationVisitsEveryPageAndRestoresSelectedDestination() {
        awaitHome()
        listOf("metronome", "chords", "sgc", "settings", "tuner", "settings", "chords", "metronome", "sgc", "tuner").forEach { route ->
            compose.onNodeWithTag("nav_$route").performClick()
            compose.onNodeWithTag("nav_$route").assertIsSelected()
            compose.waitUntil(5_000) { compose.onAllNodesWithTag("${route}_page").fetchSemanticsNodes().isNotEmpty() }
            compose.onNodeWithTag("${route}_page").assertIsDisplayed()
        }
        compose.onNodeWithTag("nav_settings").performClick()
        compose.activityRule.scenario.recreate()
        compose.waitUntil(10_000) { compose.onAllNodesWithTag("settings_page").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithTag("nav_settings").assertIsSelected()
    }

    @Test fun customTuningCanBeCreatedSelectedAndUsedByTuner() {
        awaitHome()
        compose.onNodeWithTag("nav_settings").performClick()
        compose.onNodeWithTag("open_select_tuning").performScrollTo().performClick()
        compose.waitUntil(5_000) { compose.onAllNodesWithTag("select_tuning").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Cancel").performClick()
        compose.onNodeWithTag("open_create_tuning").performScrollTo().performClick()
        compose.onNodeWithTag("tuning_name").performTextInput("Compose test tuning")
        compose.waitUntil(5_000) { compose.onAllNodesWithTag("tuning_pitch_0").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("OK").performClick()
        compose.waitUntil(5_000) {
            runBlocking {
                database.tuningDAO.getTuningById(database.tuningDAO.getLastTuningId()).name == "Compose test tuning"
            }
        }
        compose.onNodeWithTag("open_select_tuning").performScrollTo().performClick()
        compose.waitUntil(5_000) { compose.onAllNodesWithTag("select_tuning").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithTag("select_tuning").performClick()
        compose.onNodeWithText("Compose test tuning").performScrollTo().performClick()
        compose.onNodeWithText("OK").performClick()
        compose.waitUntil(5_000) { runBlocking { database.tuningDAO.getTuningNameBySettings() == "Compose test tuning" } }
        compose.onNodeWithTag("nav_tuner").performClick()
        compose.waitUntil(5_000) { compose.onAllNodesWithText("Compose test tuning").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Compose test tuning").assertExists()
    }

    @Test fun metronomeNotificationIntentOpensComposeMetronome() {
        awaitHome()
        compose.onNodeWithTag("nav_settings").performClick()
        compose.runOnIdle {
            val notificationIntent = NavDeepLinkBuilder(context)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.bottom_nav)
                .setDestination(R.id.navigation_metronome)
                .createTaskStackBuilder().intents.last()
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            context.startActivity(notificationIntent)
        }
        compose.waitUntil(5_000) { compose.onAllNodesWithTag("metronome_page").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithTag("nav_metronome").assertIsSelected()
    }
}
