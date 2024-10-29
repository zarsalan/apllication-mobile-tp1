package com.example.tp1_epicerie.ui.views

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.Category
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.ListItem
import com.example.tp1_epicerie.ui.common.AppBarMenu
import com.example.tp1_epicerie.ui.common.AppBarMenuInfo
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.GroceryItemCard
import com.example.tp1_epicerie.ui.common.GroceryItemCardInfo
import com.example.tp1_epicerie.ui.common.ListItemCard
import com.example.tp1_epicerie.ui.common.ListItemCardInfo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

// La vue pour afficher les articles d'épicerie (tous ou favoris)
@Composable
fun GroceryItemsView(
    viewModel: GroceryViewModel,
    navHostController: NavHostController,
    mode: Boolean
) {
    val groceryItemsList = remember { mutableStateListOf<GroceryItem>() }
    var itemsByCategory by remember { mutableStateOf(mapOf<Category, List<GroceryItem>>()) }

    // On met à jour les items par catégorie
    suspend fun updateItemsByCategory() {
        val updatedItemsByCategory = mutableMapOf<Category, MutableList<GroceryItem>>()
        groceryItemsList
            .forEach { groceryItem ->
                val category = viewModel.getCategoryById(groceryItem.categoryId ?: 1L).first()
                updatedItemsByCategory.getOrPut(category) { mutableListOf() }.add(groceryItem)
            }
        itemsByCategory = updatedItemsByCategory
    }

    // On obtient les items de l'épicerie selon le mode et on les trie par catégorie
    LaunchedEffect(mode) {
        Log.d("OBTAINED1", "itemsByCategory: $itemsByCategory")
        if (mode) {
            viewModel.getAllGroceryItems.collectLatest { items ->
                groceryItemsList.clear()
                groceryItemsList.addAll(items)
                updateItemsByCategory()
            }
        } else {
            viewModel.getFavoriteGroceryItems.collectLatest { items ->
                groceryItemsList.clear()
                groceryItemsList.addAll(items)
                updateItemsByCategory()
            }
        }
    }

    Scaffold(
        topBar = {
            AppBarView(
                title = if (mode) Screen.AllItems.title() else Screen.Favorites.title(),
                navHostController = navHostController,
                appBarMenuInfo = if (mode) AppBarMenuInfo(menus = listOf(
                    AppBarMenu(
                        title = stringResource(R.string.menu_addItem),
                        onClick = { navHostController.navigate(Screen.AddEditItem.route + "/0L") }
                    )
                )) else AppBarMenuInfo(menus = listOf(
                    AppBarMenu(
                        title = stringResource(R.string.menu_addItem),
                        onClick = { navHostController.navigate(Screen.AllItems.route) }
                    )
                ))
            )
        },
        // Boutton flottant pour ajouter un article
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
        // Affichage des items dans un LazyColumn (comme recyclerView mais bien meilleur)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(top = 6.dp)
        ) {
            itemsByCategory.forEach { (category, items) ->
                item {
                    Text(
                        text = category.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 20.dp, bottom = 4.dp)
                            .fillMaxWidth()
                    )
                }
                items(items) { groceryItem ->
                    GroceryItemCard(
                        viewModel = viewModel,
                        cardInfo = GroceryItemCardInfo(
                            groceryItem = groceryItem,
                            viewModel = viewModel,
                            onClick = { navHostController.navigate(Screen.AddEditItem.route + "/${groceryItem.id}") },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                }
            }
        }
    }
}