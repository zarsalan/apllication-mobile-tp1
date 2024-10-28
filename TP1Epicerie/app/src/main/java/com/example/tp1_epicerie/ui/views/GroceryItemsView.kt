package com.example.tp1_epicerie.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.ListItem
import com.example.tp1_epicerie.ui.common.AppBarMenu
import com.example.tp1_epicerie.ui.common.AppBarMenuInfo
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.GroceryItemCard
import com.example.tp1_epicerie.ui.common.GroceryItemCardInfo


@Composable
fun GroceryItemsView(
    viewModel: GroceryViewModel,
    navHostController: NavHostController,
    mode: Boolean
) {
    Scaffold(
        topBar = {
            AppBarView(
                title = if (mode) Screen.AllItems.title else Screen.Favorites.title,
                onBackNavClicked = { navHostController.popBackStack() },
                appBarMenuInfo = if (mode) AppBarMenuInfo(menus = listOf(
                    AppBarMenu(
                        title = stringResource(R.string.menu_addItem),
                        onClick = { navHostController.navigate(Screen.AddEditItem.route + "/0L") }
                    )
                )) else AppBarMenuInfo()
            )
        },
        floatingActionButton = {
            if (mode) {
                FloatingActionButton(
                    modifier = Modifier.padding(all = 20.dp),
                    contentColor = Color.White,
                    containerColor = colorResource(id = R.color.app_bar),
                    onClick = {
                        navHostController.navigate(Screen.AddEditItem.route + "/0L")
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        },
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
                    viewModel = viewModel,
                    cardInfo = GroceryItemCardInfo(
                        groceryItem = groceryItem,
                        viewModel = viewModel,
                        onClick = { navHostController.navigate(Screen.AddEditItem.route + "/${groceryItem.id}") },
                        containerColor = Color.White,
                    )
                )
            }
        }
    }
}