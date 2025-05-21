package com.example.yandexnotes.ui.screens.item.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yandexnotes.di.AppViewModelProvider
import com.example.yandexnotes.R
import com.example.yandexnotes.ui.NotesAppBar
import com.example.yandexnotes.navigation.NavigationDestination
import com.example.yandexnotes.ui.screens.item.components.NotesInputForm
import kotlinx.coroutines.launch

object CreateNoteDestination : NavigationDestination {
    override val route = "create_note"
    override val title = R.string.create_note
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateNoteViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()

    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            NotesAppBar(
                title = stringResource(CreateNoteDestination.title),
                canNavigateBack = true,
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                navigateUp = navigateBack
            )
        }
    ) { paddingValue ->

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(
                    top = paddingValue.calculateTopPadding().plus(paddingMedium),
                    start = paddingMedium,
                    end = paddingMedium,
                    bottom = paddingValue.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(paddingMedium)
        ) {
            NotesInputForm(
                noteEntity = viewModel.entryUiState.currentNote,
                onValueChange = viewModel::updateUiState,
                modifier = modifier
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveItem()
                        navigateBack()
                    }
                },
                enabled = viewModel.entryUiState.isEntryValid,
                modifier = modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.button_size))
            ) {
                Text(text = stringResource(R.string.create))
            }

            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveAndSyncItem(context)
                        navigateBack()
                    }
                },
                enabled = viewModel.entryUiState.isEntryValid,
                modifier = modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.button_size))
            ) {
                Text(text = stringResource(R.string.create_and_sync))
            }
        }
    }
}
