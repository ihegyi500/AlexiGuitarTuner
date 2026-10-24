package com.ihegyi.alexiguitartuner.commons.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ihegyi.alexiguitartuner.R.string

@Composable
fun AlexiTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) darkColorScheme(
        primary = Color(0xFFC4C0FF), secondary = Color(0xFF80CBC4)
    ) else lightColorScheme(primary = Color(0xFF39347A), secondary = Color(0xFF006A60))
    MaterialTheme(colorScheme = colors, content = content)
}

@Composable
fun ScreenColumn(tag: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().testTag(tag).verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
fun ChoiceField(label: String, selected: String, options: List<String>, tag: String, onSelect: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Box {
            OutlinedButton(
                onClick = { expanded = true }, enabled = options.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().testTag(tag)
            ) { Text(selected.ifEmpty { stringResource(string.select_option) }) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { expanded = false; onSelect(index) })
                }
            }
        }
    }
}
