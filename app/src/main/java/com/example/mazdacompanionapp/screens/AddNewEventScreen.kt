package com.example.mazdacompanionapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddNewEventScreen(onSaveEvent: (Event) -> Unit, onPresetClick: () -> Unit, selectedPreset: Preset?) {
    var name by remember { mutableStateOf("") }
    var sendEvent by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        TextField(value = sendEvent, onValueChange = { sendEvent = it }, label = { Text("Send Event") })


        Button(onClick = onPresetClick) {
            Text(text = "Select Preset")
        }
        Text(text = selectedPreset?.name ?: "No preset selected")
        Button(onClick = { onSaveEvent(Event(5 ,name, selectedPreset ?: Preset.DEFAULT, sendEvent)) }) {
            Text(text = "Save Event")
        }
    }
}
