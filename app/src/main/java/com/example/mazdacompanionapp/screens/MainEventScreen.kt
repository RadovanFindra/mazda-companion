package com.example.mazdacompanionapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mazdacompanionapp.data.Event
import com.example.mazdacompanionapp.screens.CompanionTopAppBar


object MainEventScreenDestination : NavigationDestination {
    override val route = "Main"
    override val titleRes = R.string.event_main_title
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainEventScreen(
    navigateToEventAdd:() -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ) {

    val mainUiState by viewModel.mainUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CompanionTopAppBar(
                title = stringResource(MainEventScreenDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToEventAdd,
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
                }
            )
        },
    ) {innerPadding->
        MainBody(
            eventList = mainUiState.eventList,
            onEventClick = { viewModel.changeEnableState(it)},
            modifier = modifier.fillMaxWidth(),
            contentPadding = innerPadding
        )
    }
}

@Composable
private fun MainBody(
    eventList: List<Event>,
    onEventClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (eventList.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_event_list),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(contentPadding)
            )
        }else {
            EventList(
                eventList = eventList,
                onEventClick = { onEventClick(it.id) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }

    }
}

@Composable
private fun EventList(
    eventList: List<Event>,
    onEventClick: (Event) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
){
    LazyColumn {
        items(eventList) { event ->
            EventItem(event = event, onClick = { onEventClick(event) })
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .padding(16.dp)
        ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = event.name, style = MaterialTheme.typography.h6)
                Text(text = event.preset.name, style = MaterialTheme.typography.body2)
            }

            Switch(
                checked = event.isEnabled,
                onCheckedChange = { onClick }
            )
        }
    }
}
