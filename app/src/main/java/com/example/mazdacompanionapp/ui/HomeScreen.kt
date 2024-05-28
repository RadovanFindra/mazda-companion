package com.example.mazdacompanionapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.Event
import com.example.mazdacompanionapp.EventTopAppBar
import com.example.mazdacompanionapp.R
import com.example.mazdacompanionapp.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            EventTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.event_entry_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            eventsList = homeUiState.itemList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}
@Composable
fun HomeBody(
    eventsList: List<Event>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (eventsList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_event_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(contentPadding)
            )
        } else {
            EventList(
                eventsList = eventsList,
                onItemClick = { onItemClick(it.id) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal =  dimensionResource(id = R.dimen.padding_8))
            )
        }
    }
}

@Composable
fun EventList(
    eventsList: List<Event>,
    onItemClick: (Event) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = eventsList, key = { it.id }) { event ->
            EvenetItem(event = event,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_8))
                    .clickable { onItemClick(event) })

        }
    }
}

@Composable
fun EvenetItem(
    event: Event,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
    Column (
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_20)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_8))
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = event.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = event.preset.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
        }
    }
    }
}