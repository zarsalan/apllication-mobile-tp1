package com.example.tp1_epicerie.ui.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.Category
import com.example.tp1_epicerie.data.GroceryList
import com.example.tp1_epicerie.data.ListItem
import com.example.tp1_epicerie.ui.common.AppBarMenu
import com.example.tp1_epicerie.ui.common.AppBarMenuInfo
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.ListItemCard
import com.example.tp1_epicerie.ui.common.ListItemCardInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlin.math.log

// La vue pour afficher une liste d'épicerie (liste d'articles à acheter)
@Composable
fun CustomGroceryListView(
    id: Long,
    viewModel: GroceryViewModel,
    navHostController: NavHostController
) {
    // On obtient les informations de la liste
    val groceryList = viewModel.getGroceryListById(id).collectAsState(initial = GroceryList()).value
    val groceryListItems = viewModel.getGroceryListItems(id)
        .collectAsState(initial = listOf(ListItem())).value

    val crossedItems = remember { mutableStateListOf<MutableState<ListItem>>() }
    val nonCrossedItems = remember { mutableStateListOf<MutableState<ListItem>>() }
    var itemsByCategory by remember { mutableStateOf(mapOf<Category, List<MutableState<ListItem>>>()) }

    var indexCrossed by remember { mutableStateOf(false) }
    val itemsToShow = if (indexCrossed) crossedItems else nonCrossedItems

    // Mise à jour des items par catégorie
    suspend fun updateItemsByCategory() {
        val updatedItemsByCategory = mutableMapOf<Category, MutableList<MutableState<ListItem>>>()

        itemsToShow
            .filter { it.value.isCrossed == if (indexCrossed) 1 else 0 }
            .forEach { listItem ->
                val groceryItem = viewModel.getGroceryItemById(listItem.value.groceryItemId).firstOrNull()

                if (groceryItem != null) {
                    val category = viewModel.getCategoryById(groceryItem.categoryId ?: 1L).first()
                    updatedItemsByCategory.getOrPut(category) { mutableListOf() }.add(listItem)
                }
            }

        itemsByCategory = updatedItemsByCategory
    }

    // Mise à jour des listes d'items au changement de la liste groceryListItems
    LaunchedEffect(groceryListItems) {
        crossedItems.clear()
        nonCrossedItems.clear()

        groceryListItems.forEach { listItem ->
            val statefulListItem = mutableStateOf(listItem)
            if (listItem.isCrossed == 1) {
                crossedItems.add(statefulListItem)
            } else {
                nonCrossedItems.add(statefulListItem)
            }
        }

        updateItemsByCategory()
    }

    // Mise à jour des items par catégorie au changement d'indexCrossed
    LaunchedEffect(itemsToShow, indexCrossed) {
        updateItemsByCategory()
    }

    Scaffold(
        topBar = {
            AppBarView(
                title = groceryList.title,
                navHostController = navHostController,
                appBarMenuInfo = AppBarMenuInfo(menus = listOf(
                    AppBarMenu(
                        title = stringResource(R.string.menu_addItem),
                        onClick = { navHostController.navigate(Screen.AllItems.route) }
                    ),
                    AppBarMenu(
                        title = stringResource(R.string.menu_modifyThisList),
                        onClick = { navHostController.navigate(Screen.AddEditListScreen.route + "/${groceryList.id ?: 1L}") }
                    ),
                ))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Section de sélection de liste coché / non cochées
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Boutton pour afficher les items non cochés
                Box(
                    modifier = Modifier
                        .clickable { indexCrossed = false }
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.text_unchecked) + " (${nonCrossedItems.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        textDecoration = if (!indexCrossed) TextDecoration.Underline else null,
                        textAlign = TextAlign.Center
                    )
                }

                // Boutton pour afficher les items cochés
                Box(
                    modifier = Modifier
                        .clickable { indexCrossed = true }
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.text_checked)+ " (${crossedItems.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        textDecoration = if (indexCrossed) TextDecoration.Underline else null,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Un divider pour séparer les boutons de la liste
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(150.dp)
                )
            }

            // Affichage de la liste d'items dynamiquement dans une LazyColumn (comme un RecyclerView mais bien meilleur)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
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
                    items(items) { listItem ->
                        ListItemCard(
                            viewModel = viewModel,
                            cardInfo = ListItemCardInfo(
                                listItem = listItem,
                                onClick = {navHostController.navigate(Screen.AddEditItem.route + "/${listItem.value.groceryItemId}")},
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    }
}


