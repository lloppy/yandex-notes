package com.example.yandexnotes.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.yandexnotes.R
import com.example.yandexnotes.navigation.NotesNavigation

@Composable
fun YandexNotesApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    NotesNavigation(
        navController = navController,
        modifier = modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NotesAppBar(
    title: String,
    canNavigateBack: Boolean,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = { },
    onClickSync: () -> Unit = {},
    onClickDeleteAllFromServer: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.headlineSmall) },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (canNavigateBack.not()) {
                IconButton(onClick = onClickSync) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = stringResource(R.string.sync)
                    )
                }
                IconButton(onClick = onClickDeleteAllFromServer) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            }
        }
    )
}
