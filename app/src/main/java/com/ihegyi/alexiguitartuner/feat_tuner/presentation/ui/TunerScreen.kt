package com.ihegyi.alexiguitartuner.feat_tuner.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ihegyi.alexiguitartuner.R.string
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.presentation.ui.ScreenColumn
import com.ihegyi.alexiguitartuner.feat_tuner.presentation.state.PitchButtonsUIState
import com.ihegyi.alexiguitartuner.feat_tuner.presentation.viewmodel.TunerViewModel

@Composable
fun TunerRoute(viewModel: TunerViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    var granted by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted = it }
    val pitch by viewModel.detectedPitch.collectAsStateWithLifecycle()
    val buttons by viewModel.pitchButtonsUIState.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) { viewModel.initPitchButtons() }
    DisposableEffect(owner, granted, viewModel) {
        fun synchronizeAudio() {
            if (granted && owner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                viewModel.startAudioProcessing()
            } else {
                viewModel.stopAudioProcessing()
                viewModel.stopPitchGeneration()
            }
        }
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                granted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            }
            synchronizeAudio()
        }
        owner.lifecycle.addObserver(observer)
        granted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        synchronizeAudio()
        onDispose {
            owner.lifecycle.removeObserver(observer)
            viewModel.stopAudioProcessing()
            viewModel.stopPitchGeneration()
        }
    }
    TunerScreen(pitch, buttons, granted,
        { launcher.launch(Manifest.permission.RECORD_AUDIO) }, viewModel::startPitchGeneration, viewModel::stopPitchGeneration)
}

@Composable
fun TunerScreen(pitch: Pitch, buttons: PitchButtonsUIState, permissionGranted: Boolean,
    onRequestPermission: () -> Unit, onPlayPitch: (Double) -> Unit, onStop: () -> Unit) {
    ScreenColumn("tuner_page") {
        Text(stringResource(string.title_tuner), style = MaterialTheme.typography.headlineMedium)
        if (!permissionGranted) {
            Text(stringResource(string.microphone_explanation))
            Button(onClick = onRequestPermission, modifier = Modifier.testTag("grant_microphone")) {
                Text(stringResource(string.grant_microphone))
            }
        }
        Text(pitch.name, style = MaterialTheme.typography.displayLarge, modifier = Modifier.testTag("detected_pitch"))
        Text(stringResource(string.frequency_hz, pitch.frequency), modifier = Modifier.testTag("detected_frequency"))
        Text(buttons.tuningName, style = MaterialTheme.typography.titleLarge)
        buttons.pitchList.forEachIndexed { index, target ->
            OutlinedButton(onClick = { onPlayPitch(target.frequency) },
                modifier = Modifier.fillMaxWidth().testTag("pitch_$index")) { Text(target.name) }
        }
        Button(onClick = onStop, modifier = Modifier.testTag("stop_pitch")) { Text(stringResource(string.stop)) }
    }
}
