package com.example.yandexnotes.ui.screens.item.edit

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

object EditNoteDestination : NavigationDestination {
    override val route = "edit_note"
    override val title = R.string.item_edit_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditNoteViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    val paddingMedium = dimensionResource(R.dimen.padding_medium)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            NotesAppBar(
                title = stringResource(EditNoteDestination.title),
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
                        viewModel.updateItem()
                        navigateBack()
                    }
                },
                enabled = viewModel.entryUiState.isEntryValid,
                modifier = modifier.fillMaxWidth().height(dimensionResource(R.dimen.button_size))
            ) {
                Text(text = stringResource(R.string.save))
            }

            OutlinedButton (
                onClick = {
                    coroutineScope.launch {
                        viewModel.updateAndSyncItem(context)
                        navigateBack()
                    }
                },
                enabled = viewModel.entryUiState.isEntryValid,
                modifier = modifier.fillMaxWidth().height(dimensionResource(R.dimen.button_size))
            ) {
                Text(text = stringResource(R.string.save_and_sync))
            }
        }
    }
}