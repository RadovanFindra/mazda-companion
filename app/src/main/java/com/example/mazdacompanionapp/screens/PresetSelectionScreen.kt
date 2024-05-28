package com.example.mazdacompanionapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PresetSelectionScreen(onPresetSelected: (Preset) -> Unit) {
    val presets = listOf(Preset.DEFAULT, Preset.MUSIC_MESSAGE, Preset.EVENTS)
    LazyColumn {
        items(presets) { preset ->
            PresetItem(preset = preset, onClick = { onPresetSelected(preset) })
        }
    }
}

@Composable
fun PresetItem(preset: Preset, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { onClick() }) {
        Text(text = preset.name, style = MaterialTheme.typography.h6)
    }
}
