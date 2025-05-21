package com.example.yandexnotes.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.yandexnotes.ui.screens.home.HomeDestination
import com.example.yandexnotes.ui.screens.home.HomeScreen
import com.example.yandexnotes.ui.screens.item.create.CreateNoteDestination
import com.example.yandexnotes.ui.screens.item.create.CreateNoteScreen
import com.example.yandexnotes.ui.screens.item.edit.EditNoteDestination
import com.example.yandexnotes.ui.screens.item.edit.EditNoteScreen

@Composable
fun NotesNavigation(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                onClickAddItem = {
                    navController.navigate(CreateNoteDestination.route)
                },
                onClickEdit = {
                    navController.navigate("${EditNoteDestination.route}/$it")
                },
                modifier = Modifier
            )
        }

        composable(
            route = EditNoteDestination.routeWithArgs,
            arguments = listOf(navArgument(EditNoteDestination.itemIdArg) {
                type = NavType.StringType
            })
        ) {
            EditNoteScreen(
                navigateBack = { navController.navigateUp() },
                modifier = Modifier
            )
        }

        composable(route = CreateNoteDestination.route) {
            CreateNoteScreen(
                navigateBack = { navController.navigateUp() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
