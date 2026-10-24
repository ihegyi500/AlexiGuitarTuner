package com.ihegyi.alexiguitartuner.commons.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.ihegyi.alexiguitartuner.R.drawable
import com.ihegyi.alexiguitartuner.R.string
import com.ihegyi.alexiguitartuner.feat_tuner.presentation.ui.TunerRoute
import com.ihegyi.alexiguitartuner.feat_metronome.presentation.ui.MetronomeRoute
import com.ihegyi.alexiguitartuner.feat_chordtable.presentation.ui.ChordTableRoute
import com.ihegyi.alexiguitartuner.feat_sgc.presentation.ui.SGCRoute
import kotlinx.coroutines.delay

private enum class Destination(val route: String, val label: Int, val icon: Int) {
    TUNER("tuner", string.title_tuner, drawable.ic_baseline_music_note_24),
    METRONOME("metronome", string.title_metronome, drawable.ic_baseline_speed_24),
    CHORDS("chords", string.title_chords, drawable.ic_baseline_queue_music_24),
    SGC("sgc", string.title_sgc, drawable.ic_baseline_calculate_24),
    SETTINGS("settings", string.title_settings, drawable.ic_baseline_settings_24)
}

@Composable
fun AlexiApp(navigationRequest: Pair<Int, String>? = null) {
    var splash by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) { delay(2000); splash = false }
    if (splash) {
        Surface(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(stringResource(string.alexi_guitar_tuner), style = MaterialTheme.typography.headlineLarge)
                Text(stringResource(string.created_by_istvan_hegyi))
            }
        }
        return
    }
    val controller = rememberNavController()
    val backStack by controller.currentBackStackEntryAsState()
    LaunchedEffect(navigationRequest) {
        navigationRequest?.let { request ->
            controller.navigate(request.second) {
                popUpTo(controller.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    Scaffold(bottomBar = {
        NavigationBar {
            Destination.entries.forEach { destination ->
                NavigationBarItem(selected = backStack?.destination?.route == destination.route,
                    onClick = {
                        controller.navigate(destination.route) {
                            popUpTo(controller.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(painterResource(destination.icon), contentDescription = stringResource(destination.label)) },
                    label = { Text(stringResource(destination.label), maxLines = 2) },
                    modifier = Modifier.testTag("nav_${destination.route}"))
            }
        }
    }) { padding ->
        NavHost(controller, startDestination = "tuner", modifier = Modifier.padding(padding)) {
            composable("tuner") { TunerRoute() }
            composable("metronome") { MetronomeRoute() }
            composable("chords") { ChordTableRoute() }
            composable("sgc") { SGCRoute() }
            composable("settings") { UserSettingsRoute() }
        }
    }
}
