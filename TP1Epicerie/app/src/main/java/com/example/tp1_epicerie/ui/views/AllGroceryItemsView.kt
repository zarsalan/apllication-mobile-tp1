package com.example.tp1_epicerie.ui.views

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.ui.common.AppBarView


@Composable
fun AllGroceryItemsView(viewModel: GroceryViewModel, navHostController: NavHostController) {
    Scaffold(
        topBar = {
            AppBarView(title = Screen.AllItems.title, onBackNavClicked = { navHostController.popBackStack() })
        }
    ) {  }
}