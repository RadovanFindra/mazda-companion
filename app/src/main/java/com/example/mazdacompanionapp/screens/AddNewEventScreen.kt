package com.example.mazdacompanionapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mazdacompanionapp.Event
import com.example.mazdacompanionapp.NavigationDestination
import com.example.mazdacompanionapp.Preset
import com.example.mazdacompanionapp.R


object EventAddDestination : NavigationDestination {
    override val route = "event_add"
    override val titleRes = R.string.event_add_title
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddNewEventScreen(
    onSaveEvent: (Event) -> Unit,
    onPresetClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CompanionTopAppBar(
                title = "ADD",
                canNavigateBack = true,
                navigateUp = {})
        }
    ) {innerPadding ->
        AddEventBody(
            onSaveEvent = onSaveEvent,
            onPresetClick = onPresetClick,
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanionTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
)
{
    CenterAlignedTopAppBar(title = { androidx.compose.material3.Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun AddEventBody(
    onSaveEvent: (Event) -> Unit,
    onPresetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf(Preset.NOT_SELECTED) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(text = selectedPreset.title)
                }


                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Preset.values().forEach { preset ->
                        if (preset.title != "Select Preset") {
                            DropdownMenuItem(
                                onClick = {
                                    selectedPreset = preset
                                    expanded = false
                                }

                            ) {
                                Text(text = preset.title)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    onSaveEvent(
                        Event(
                            name,
                            selectedPreset,
                            mutableStateOf(true)
                        )
                    )
                }
            ) {
                Text(text = "Save Event")
            }
        }
    }
}

