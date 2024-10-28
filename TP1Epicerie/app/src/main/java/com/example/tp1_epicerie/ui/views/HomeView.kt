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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.ui.common.AppBarMenu
import com.example.tp1_epicerie.ui.common.AppBarMenuInfo
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomListCardInfo
import com.example.tp1_epicerie.ui.common.CustomListCard

@Composable
fun HomeView(viewModel: GroceryViewModel, navHostController: NavHostController) {
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppBarView(title = Screen.HomeScreen.title(),
                appBarMenuInfo = AppBarMenuInfo(menus = listOf(
                    AppBarMenu(
                        title = stringResource(R.string.menu_addItem),
                        onClick = { navHostController.navigate(Screen.AddEditItem.route + "/0L") }
                    ),
                    AppBarMenu(
                        title = stringResource(R.string.menu_addList),
                        onClick = { navHostController.navigate(Screen.AddEditListScreen.route + "/0L") }
                    ),
                    AppBarMenu(
                        title = stringResource(R.string.menu_addCategory),
                        onClick = { navHostController.navigate(Screen.AddEditCategory.route + "/0L") }
                    ),
                    AppBarMenu(
                        title = stringResource(R.string.menu_modifyCategories),
                        onClick = { navHostController.navigate(Screen.Categories.route) }
                    ),
                    AppBarMenu(
                        title = stringResource(R.string.menu_settings),
                        onClick = { navHostController.navigate(Screen.Settings.route) }
                    ),
                    AppBarMenu(
                        title = stringResource(R.string.menu_about),
                        onClick = { showAboutDialog = true }
                    )
                ))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.app_bar),
                onClick = {
                    navHostController.navigate(Screen.AddEditListScreen.route + "/0L")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
    ) {

        val groceryLists = viewModel.getAllGroceryLists.collectAsState(initial = emptyList())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it).padding(top = 6.dp)
        ) {
            item {
                CustomListCard(
                    viewModel,
                    navHostController,
                    CustomListCardInfo(
                        title = stringResource(R.string.listCard_allItem),
                        description = stringResource(R.string.listCard_seeItem),
                        onClick = { navHostController.navigate(Screen.AllItems.route) },
                        containerColor = colorResource(id = R.color.all_items)
                    )
                )
            }
            item {
                CustomListCard(
                    viewModel,
                    navHostController,
                    CustomListCardInfo(
                        title = stringResource(R.string.listCard_favorites),
                        description = stringResource(R.string.listCard_viewFavorite),
                        onClick = { navHostController.navigate(Screen.Favorites.route) },
                        containerColor = colorResource(id = R.color.favorite_items)
                    )
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 16.dp)
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

            items(groceryLists.value) { grocery ->
                CustomListCard(
                    viewModel,
                    navHostController,
                    CustomListCardInfo(
                        listId = grocery.id,
                        title = grocery.title,
                        description = grocery.description,
                        onClick = { navHostController.navigate(Screen.GroceryList.route + "/${grocery.id}") },
                        containerColor = Color.White,
                        canEdit = true,
                        canDelete = true,
                        groceryList = grocery,
                    )
                )
            }
        }
    }

    // Dialogue d'à propos
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text(stringResource(R.string.menu_about)) },
            text = {
                Text(
                    text = stringResource(R.string.about_text),
                    modifier = Modifier.padding(top = 20.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = { showAboutDialog = false }
                ) {
                    Text(stringResource(R.string.menu_close))
                }
            },
        )
    }
}