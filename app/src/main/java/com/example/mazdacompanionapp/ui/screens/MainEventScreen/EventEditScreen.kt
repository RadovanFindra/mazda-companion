package com.example.mazdacompanionapp.ui.screens.MainEventScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mazdacompanionapp.AppViewModelProvider
import com.example.mazdacompanionapp.R
import com.example.mazdacompanionapp.ui.Navigation.NavigationDestination
import com.example.mazdacompanionapp.ui.screens.MainEventScreen.ViewModel.EventEditViewModel

object EventEditScreenDestination : NavigationDestination {
    override val route = "Event_edit_screen"
    override val titleRes = R.string.event_edit_title
    const val eventIdArg = "eventId"
    val routeWithArgs = "$route/{$eventIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEvent(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.eventEditUiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.tertiaryContainer,
                    titleContentColor = colorScheme.onTertiaryContainer,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.event_edit_title),
                        color = colorScheme.onTertiaryContainer
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Text(text = "DA", Modifier.padding(innerPadding), color = colorScheme.onSurface)
//        EditBluetoothItemBody(
//            deviceDetails = uiState.deviceDetails,
//            onDeviceValueChange = viewModel::updateUiState,
//            events = uiState.events,
//            onSaveClick = {
//                coroutineScope.launch {
//                    viewModel.updateDevice(uiState.deviceDetails)
//                    navigateBack()
//                }
//            },
//            modifier = Modifier
//                .padding(
//                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
//                    top = innerPadding.calculateTopPadding(),
//                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
//                )
//
//        )
//    }
    }
}