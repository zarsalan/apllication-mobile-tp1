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
import com.example.tp1_epicerie.ui.views.AddEditCategoryView
import com.example.tp1_epicerie.ui.views.AddEditItemView
import com.example.tp1_epicerie.ui.views.AddEditListView
import com.example.tp1_epicerie.ui.views.HomeView
import com.example.tp1_epicerie.ui.views.CategoriesView
import com.example.tp1_epicerie.ui.views.CustomGroceryListView
import com.example.tp1_epicerie.ui.views.GroceryItemsView
import com.example.tp1_epicerie.ui.views.SettingsView

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
        // Page principal
        composable(Screen.HomeScreen.route) {
            HomeView(viewModel, navHostController)
        }

        // Affichage de tous les articles
        composable(Screen.AllItems.route) {
            GroceryItemsView(viewModel, navHostController, true)
        }

        // Affichage des articles favoris
        composable(Screen.Favorites.route) {
            GroceryItemsView(viewModel, navHostController, false)
        }

        // Affichage d'une liste custom
        composable(
            Screen.GroceryList.route + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType; defaultValue = 0L; nullable
            })
        ) {
            val id = it.arguments?.getLong("id") ?: 0L
            CustomGroceryListView(id, viewModel, navHostController)
        }

        // Ajout ou modification d'une liste
        composable(
            Screen.AddEditListScreen.route + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType; defaultValue = 0L; nullable
            })
        ) {
            val id = it.arguments?.getLong("id") ?: 0L
            AddEditListView(id, viewModel, navHostController)
        }

        // Ajout ou modification d'un article
        composable(
            Screen.AddEditItem.route + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType; defaultValue = 0L; nullable
            })
        ) {
            val id = it.arguments?.getLong("id") ?: 0L
            AddEditItemView(id, viewModel, navHostController)
        }

        // Ajout ou modification d'une catégorie
        composable(Screen.AddEditCategory.route + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType; defaultValue = 0L; nullable
            })
        ) {
            val id = it.arguments?.getLong("id") ?: 0L
            AddEditCategoryView(id, viewModel, navHostController)
        }

        // Affichage de tous les catégories
        composable(Screen.Categories.route) {
            CategoriesView(viewModel, navHostController)
        }

        // Paramètres
        composable(Screen.Settings.route) {
            SettingsView(viewModel, navHostController)
        }
    }
}