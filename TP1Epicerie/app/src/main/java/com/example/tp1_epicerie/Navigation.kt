package com.example.tp1_epicerie

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import com.example.tp1_epicerie.ui.home.HomeView
import com.example.tp1_epicerie.ui.list.AllGroceryItems
import com.example.tp1_epicerie.ui.list.FavoriteGroceryItems

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    viewModel: GroceryViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
        modifier = modifier
    ) {
        composable(Screen.HomeScreen.route) {
            HomeView(viewModel, navController)
        }
        composable(Screen.AllItems.route){
            AllGroceryItems(viewModel, navController)
        }
        composable(Screen.Favorites.route){
            FavoriteGroceryItems(viewModel, navController)
        }
    }
}