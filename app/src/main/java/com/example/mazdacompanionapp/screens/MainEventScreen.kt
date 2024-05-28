package com.example.mazdacompanionapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainEventScreen(
    onAddEventClick: () -> Unit,
    events: List<Event>,
    onEventClick: (Event) -> Unit,
) {
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


