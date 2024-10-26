package com.example.tp1_epicerie

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AllGroceryItems(viewModel: GroceryViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            AppBarView(title = Screen.AllItems.title, onBackNavClicked = { navController.popBackStack() })
        }
    ) {  }
}