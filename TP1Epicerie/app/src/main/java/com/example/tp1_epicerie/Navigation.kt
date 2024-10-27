package com.example.tp1_epicerie

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.tp1_epicerie.ui.views.AddEditListView
import com.example.tp1_epicerie.ui.views.HomeView
import com.example.tp1_epicerie.ui.views.AllGroceryItemsView
import com.example.tp1_epicerie.ui.views.CustomGroceryListView
import com.example.tp1_epicerie.ui.views.FavoriteGroceryItemsView

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    viewModel: GroceryViewModel = viewModel(),
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.HomeScreen.route,
        modifier = modifier
    ) {
        composable(Screen.HomeScreen.route) {
            HomeView(viewModel, navHostController)
        }
        composable(Screen.AllItems.route) {
            AllGroceryItemsView(viewModel, navHostController)
        }
        composable(Screen.Favorites.route) {
            FavoriteGroceryItemsView(viewModel, navHostController)
        }
        composable(
            Screen.AddEditListScreen.route + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType; defaultValue = 0L; nullable
            })
        ) {
            val id = it.arguments?.getLong("id") ?: 0L
            AddEditListView(id, viewModel, navHostController)
        }
        composable(
            Screen.GroceryList.route + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType; defaultValue = 0L; nullable
            })
        ) {
            val id = it.arguments?.getLong("id") ?: 0L
            CustomGroceryListView(id, viewModel, navHostController)
        }
    }
}