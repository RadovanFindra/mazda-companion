package com.example.mazdacompanionapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun MainEventScreen(onAddEventClick: () -> Unit, events: List<Event>, onEventClick: (Event) -> Unit) {
    Column {
        LazyColumn {
            items(events) { event ->
                EventItem(event = event, onClick = { onEventClick(event) })
            }
        }
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddEventClick,
                    content = {
                        Icon(Icons.Default.Add, contentDescription = "Add Event")
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
        ) content@{

        }
    }
}

@Composable
fun EventItem(event: Event, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp).clickable(onClick = onClick)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = event.name, style = MaterialTheme.typography.h6)
                Text(text = event.preset.name, style = MaterialTheme.typography.body2)
            }

            Switch(
                checked = event.isEnabled.value,
                onCheckedChange = { event.isEnabled.value = it}
            )
        }
    }
}
