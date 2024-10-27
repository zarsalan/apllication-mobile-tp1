package com.example.tp1_epicerie.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomListCardInfo
import com.example.tp1_epicerie.ui.common.CustomListCard

@Composable
fun HomeView(viewModel: GroceryViewModel, navHostController: NavHostController) {
    Scaffold(topBar = { AppBarView(title = Screen.HomeScreen.title) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.app_bar),
                onClick = {
                    navHostController.navigate(Screen.AddEditListScreen.route)
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        val groceryList = viewModel.getAllGroceryLists.collectAsState(initial = emptyList())
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            item {
                CustomListCard(
                    CustomListCardInfo(
                        "Tous les articles",
                        "Voir tous les articles",
                        { navHostController.navigate(Screen.AllItems.route) },
                        colorResource(id = R.color.all_items)
                    )
                )
            }
            item {
                CustomListCard(
                    CustomListCardInfo(
                        "Favoris",
                        "Voir les articles favoris",
                        { navHostController.navigate(Screen.Favorites.route) },
                        colorResource(id = R.color.favorite_items)
                    )
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 10.dp)
                ) {
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(150.dp)
                    )
                }
            }

            items(groceryList.value) { grocery ->
                CustomListCard(
                    CustomListCardInfo(
                        grocery.title,
                        grocery.description,
                        { navHostController.navigate(Screen.GroceryList.route + "/${grocery.id}") },
                        Color.White
                    )
                )
            }
        }
    }
}