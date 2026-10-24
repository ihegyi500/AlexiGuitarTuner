package com.ihegyi.alexiguitartuner.feat_sgc.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ihegyi.alexiguitartuner.R.string
import com.ihegyi.alexiguitartuner.commons.domain.entities.InstrumentString
import com.ihegyi.alexiguitartuner.feat_sgc.presentation.viewmodel.SGCViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SGCRoute(viewModel: SGCViewModel = hiltViewModel()) {
    val strings by viewModel.listOfStrings.collectAsStateWithLifecycle()
    SGCScreen(strings, viewModel::insertString, viewModel::deleteString,
        { frequency -> withContext(Dispatchers.IO) { viewModel.getPitch(frequency)?.name.orEmpty() } },
        viewModel::calculateStringGauge) { instrumentString, name ->
        val pitch = withContext(Dispatchers.IO) { viewModel.getPitchByName(name) }
        if (pitch == null) false else {
            viewModel.updateString(instrumentString.copy(frequency = pitch.frequency))
            true
        }
    }
}

@Composable
fun SGCScreen(strings: List<InstrumentString>, onAdd: () -> Unit, onDelete: (InstrumentString) -> Unit,
    pitchName: suspend (Double) -> String, gauge: (InstrumentString) -> String,
    onCalculate: suspend (InstrumentString, String) -> Boolean) {
    var showInfo by rememberSaveable { mutableStateOf(false) }
    LazyColumn(Modifier.fillMaxSize().testTag("sgc_page"), contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text(stringResource(string.title_sgc), style = MaterialTheme.typography.headlineMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onAdd, modifier = Modifier.testTag("add_string")) { Text(stringResource(string.add_string)) }
                OutlinedButton(onClick = { showInfo = true }, modifier = Modifier.testTag("calculator_info")) { Text(stringResource(string.about_calculator)) }
            }
        }
        items(strings, key = { it.stringNumber }) { instrumentString ->
            StringEditor(instrumentString, pitchName, gauge, onCalculate, { onDelete(instrumentString) })
        }
    }
    if (showInfo) AlertDialog(onDismissRequest = { showInfo = false },
        title = { Text(stringResource(string.about_calculator)) },
        text = { Text(stringResource(string.info), Modifier.verticalScroll(rememberScrollState())) },
        confirmButton = { TextButton(onClick = { showInfo = false }) { Text(stringResource(string.accept)) } })
}

@Composable
private fun StringEditor(instrumentString: InstrumentString, pitchName: suspend (Double) -> String,
    gauge: (InstrumentString) -> String, onCalculate: suspend (InstrumentString, String) -> Boolean, onDelete: () -> Unit) {
    var name by rememberSaveable(instrumentString.frequency) { mutableStateOf("") }
    var length by rememberSaveable(instrumentString.scaleLength) { mutableStateOf(instrumentString.scaleLength.toString()) }
    var tension by rememberSaveable(instrumentString.tension) { mutableStateOf(instrumentString.tension.toString()) }
    var invalid by remember { mutableStateOf(false) }
    var saving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(instrumentString.frequency) { name = pitchName(instrumentString.frequency) }
    val number = instrumentString.stringNumber
    Card(Modifier.fillMaxWidth().testTag("string_$number")) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(string.string_number, number), style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(name, { name = it; invalid = false }, label = { Text(stringResource(string.pitch_name)) },
                singleLine = true, modifier = Modifier.fillMaxWidth().testTag("string_pitch_$number"))
            OutlinedTextField(length, { length = it; invalid = false }, label = { Text(stringResource(string.scale_length)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("string_length_$number"))
            OutlinedTextField(tension, { tension = it; invalid = false }, label = { Text(stringResource(string.tension)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("string_tension_$number"))
            Text("${stringResource(string.string_gauge)} ${gauge(instrumentString)}", Modifier.testTag("string_gauge_$number"))
            if (invalid) Text(stringResource(string.invalid_string_input), color = MaterialTheme.colorScheme.error)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val parsedLength = length.toDoubleOrNull()
                    val parsedTension = tension.toDoubleOrNull()
                    if (parsedLength == null || !parsedLength.isFinite() || parsedLength <= 0 ||
                        parsedTension == null || !parsedTension.isFinite() || parsedTension <= 0 || name.isBlank()) invalid = true
                    else scope.launch {
                        saving = true
                        try { invalid = !onCalculate(instrumentString.copy(scaleLength = parsedLength, tension = parsedTension), name.trim()) }
                        finally { saving = false }
                    }
                }, enabled = !saving, modifier = Modifier.testTag("calculate_$number")) { Text(stringResource(string.calculate)) }
                TextButton(onClick = onDelete, modifier = Modifier.testTag("delete_string_$number")) { Text(stringResource(string.delete)) }
            }
        }
    }
}
