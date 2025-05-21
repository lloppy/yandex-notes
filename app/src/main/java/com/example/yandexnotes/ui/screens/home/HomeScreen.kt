package com.example.yandexnotes.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yandexnotes.di.AppViewModelProvider
import com.example.yandexnotes.R
import com.example.yandexnotes.navigation.NavigationDestination
import com.example.yandexnotes.ui.NotesAppBar
import com.example.yandexnotes.ui.screens.home.components.NoteCard
import com.example.yandexnotes.ui.screens.home.components.SwipeableWrapper
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val title = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClickAddItem: () -> Unit,
    onClickEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            NotesAppBar(
                title = stringResource(HomeDestination.title),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onClickSync = {
                    coroutineScope.launch {
                        viewModel.syncFromServer()
                    }
                },
                onClickDeleteAllFromServer = {
                    coroutineScope.launch {
                        viewModel.deleteAllFromServer()
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickAddItem,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValue ->

        when (val state = uiState) {
            is HomeNotesState.Loading -> {
                LoadingScreen()
            }

            is HomeNotesState.Success -> {
                HabitContent(
                    notes = state.notes,
                    onClickNote = {
                        coroutineScope.launch {
                            onClickEdit.invoke(it)
                        }
                    },
                    onSwipeDelete = {
                        coroutineScope.launch {
                            viewModel.deleteNoteFromServer(it)
                        }
                    },
                    onSwipeEdit = {
                        coroutineScope.launch {
                            onClickEdit.invoke(it)
                        }
                    },
                    modifier = modifier,
                    contentPadding = paddingValue
                )
            }

            is HomeNotesState.Error -> {
                ErrorMessage(
                    state = state,
                    modifier = modifier,
                    contentPadding = paddingValue
                )
            }
        }
    }
}

@Composable
fun HabitContent(
    notes: List<com.example.model.Note>,
    onClickNote: (String) -> Unit,
    onSwipeDelete: (String) -> Unit,
    onSwipeEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    if (notes.isEmpty()) {
        Text(
            text = stringResource(R.string.no_items),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.fillMaxWidth(),
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(dimensionResource(R.dimen.min_habit_card_width)),
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.Top
        ) {
            items(items = notes, key = { it.uid }) { note ->
                SwipeableWrapper(
                    onSwipeDelete = {
                        onSwipeDelete(note.uid)
                    },
                    content = {
                        NoteCard(
                            note = note,
                            onClickDelete = {
                                onSwipeDelete(note.uid)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(3f)
                                .clickable(onClick = { onClickNote(note.uid) })
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = dimensionResource(R.dimen.padding_small),
                            horizontal = dimensionResource(R.dimen.padding_medium)
                        )
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(dimensionResource(R.dimen.loading_circle)))
    }
}

@Composable
fun ErrorMessage(
    state: HomeNotesState.Error,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(text = state.message, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
    }
}
