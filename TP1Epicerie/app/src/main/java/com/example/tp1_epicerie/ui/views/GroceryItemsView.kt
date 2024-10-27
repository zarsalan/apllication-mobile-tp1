package com.example.tp1_epicerie.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.ListItem
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.GroceryItemCard
import com.example.tp1_epicerie.ui.common.GroceryItemCardInfo


@Composable
fun GroceryItemsView(viewModel: GroceryViewModel, navHostController: NavHostController, mode: Boolean) {
    Scaffold(
        topBar = {
            AppBarView(title = Screen.AllItems.title, onBackNavClicked = { navHostController.popBackStack() })
        }
    ) {
        val groceryItemsList = remember { mutableStateListOf<GroceryItem>() }

        LaunchedEffect(mode) {
            if (mode) {
                viewModel.getAllGroceryItems.collect { items ->
                    groceryItemsList.clear()
                    groceryItemsList.addAll(items)
                }
            } else {
                viewModel.getFavoriteGroceryItems.collect { items ->
                    groceryItemsList.clear()
                    groceryItemsList.addAll(items)
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(groceryItemsList) { groceryItem ->
                GroceryItemCard(
                    GroceryItemCardInfo(
                        groceryItem = groceryItem,
                        viewModel = viewModel,
                        onClick = { /*Lien vers la page de l'item*/ },
                        containerColor = Color.White,
                        canFavorite = true,
                        canDelete = true
                    )
                )
            }
        }
    }
}