package com.example.tp1_epicerie.ui.views

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.Category
import com.example.tp1_epicerie.data.GroceryList
import com.example.tp1_epicerie.data.ListItem
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomListCard
import com.example.tp1_epicerie.ui.common.CustomListCardInfo
import com.example.tp1_epicerie.ui.common.ListItemCard
import com.example.tp1_epicerie.ui.common.ListItemCardInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

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

    val crossedItems = remember { mutableStateListOf<ListItem>() }
    val nonCrossedItems = remember { mutableStateListOf<ListItem>() }

    val itemsByCategory = remember { mutableMapOf<Category, MutableList<ListItem>>() }

    LaunchedEffect(groceryList) {
        groceryListItems.forEach { listItem ->
            if (listItem.isCrossed == 1) {
                crossedItems.add(listItem)
            } else {
                nonCrossedItems.add(listItem)
            }
        }
    }

    val indexCrossed = remember { mutableStateOf(false) }
    val itemsToShow = if (indexCrossed.value) crossedItems else nonCrossedItems

    LaunchedEffect(itemsToShow, indexCrossed.value) {
        itemsByCategory.clear()
        itemsToShow
            .filter { it.isCrossed == if (indexCrossed.value) 1 else 0 }
            .forEach { listItem ->
                val groceryItem = viewModel.getGroceryItemById(listItem.groceryItemId).firstOrNull()

                if (groceryItem != null) {
                    Log.d("OBTAINED", "listItem: $listItem")
                    val category = viewModel.getCategoryById(groceryItem.categoryId ?: 0L).first()
                    itemsByCategory.getOrPut(category) { mutableListOf() }.add(listItem)
                    Log.d("OBTAINED", "itemsByCategory: $itemsByCategory")
                }
            }
    }


    Scaffold(
        topBar = {
            AppBarView(
                title = groceryList.title,
                onBackNavClicked = { navHostController.popBackStack() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Section de sélection Crossed / Uncrossed
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Uncrossed",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable { indexCrossed.value = false }
                        .padding(8.dp),
                    textDecoration = if (!indexCrossed.value) TextDecoration.Underline else null
                )
                Text(
                    text = "Crossed",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable { indexCrossed.value = true }
                        .padding(8.dp),
                    textDecoration = if (indexCrossed.value) TextDecoration.Underline else null
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(150.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                itemsByCategory.forEach { (category, items) ->
                    item {
                        Text(
                            text = category.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                    }

                    items(items) { listItem ->
                        ListItemCard(
                            ListItemCardInfo(
                                listItem = listItem,
                                viewModel = viewModel,
                                onClick = { /*Lien vers la page de l'item*/ },
                                containerColor = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}


