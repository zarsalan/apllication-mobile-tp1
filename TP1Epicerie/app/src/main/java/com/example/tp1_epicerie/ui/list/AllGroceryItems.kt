package com.example.tp1_epicerie.ui.list

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.ui.common.AppBarView


@Composable
fun AllGroceryItems(viewModel: GroceryViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            AppBarView(title = Screen.AllItems.title, onBackNavClicked = { navController.popBackStack() })
        }
    ) {  }
}