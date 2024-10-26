package com.example.tp1_epicerie

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun FavoriteGroceryItems(viewModel: GroceryViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            AppBarView(
                title = Screen.Favorites.title,
                onBackNavClicked = { navController.popBackStack() })
        }
    ) { }
}