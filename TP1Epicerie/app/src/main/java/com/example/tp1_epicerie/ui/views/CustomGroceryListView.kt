package com.example.tp1_epicerie.ui.views

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.data.GroceryList
import com.example.tp1_epicerie.ui.common.AppBarView

@Composable
fun CustomGroceryListView(
    id: Long,
    viewModel: GroceryViewModel,
    navHostController: NavHostController
) {
    // On obtient les informations de la liste
    val groceryList = viewModel.getGroceryListById(id)
        .collectAsState(initial = GroceryList(0L, "", "", listOf<Long>())).value

    Scaffold(topBar = {
        AppBarView(
            title = groceryList.title,
            onBackNavClicked = { navHostController.popBackStack() })
    }) { }
}