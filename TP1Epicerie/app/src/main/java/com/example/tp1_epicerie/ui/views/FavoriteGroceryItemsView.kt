package com.example.tp1_epicerie.ui.views

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.ui.common.AppBarView

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