package com.example.mazdacompanionapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mazdacompanionapp.Event

@Composable
fun EventItem(event: Event, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp).clickable(onClick = onClick)) {
        Text(text = event.name)
        Text(text = event.sendEvent)

    }
}
