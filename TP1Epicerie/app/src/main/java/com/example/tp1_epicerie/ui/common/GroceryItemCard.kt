package com.example.tp1_epicerie.ui.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.GroceryList
import com.example.tp1_epicerie.data.ListItem

data class GroceryItemCardInfo(
    val groceryItem: GroceryItem,
    val viewModel: GroceryViewModel,
    val onClick: () -> Unit,
    val containerColor: Color,
    val canFavorite: Boolean = false,
    val canDelete: Boolean = false
)

@Composable
fun GroceryItemCard(
    cardInfo: GroceryItemCardInfo
) 
{
    var showDeleteDialog by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val groceryLists = cardInfo.viewModel.getAllGroceryLists.collectAsState(initial = emptyList()).value
    val appBarMenuInfo: AppBarMenuInfo = AppBarMenuInfo(
        groceryLists.map { groceryList ->
            AppBarMenu(
                title = groceryList.title,
                onClick = {
                    cardInfo.viewModel.upsertListItem(
                        ListItem(
                            itemId = cardInfo.groceryItem.id,
                            quantity = 1,
                            isCrossed = 0
                        )
                    )
                }
            )
        }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp, start = 3.dp, end = 6.dp)
            .clickable { cardInfo.onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardInfo.containerColor,
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(max = 200.dp)
            ) {
                Text(text = cardInfo.groceryItem.name, fontWeight = FontWeight.ExtraBold)
                Text(
                    text = cardInfo.groceryItem.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (cardInfo.groceryItem.imagePath != null) {
                    androidx.compose.foundation.Image(
                        painter = rememberAsyncImagePainter(cardInfo.groceryItem.imagePath),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
                IconButton(onClick = {
                    menuExpanded = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.Black
                    )
                }
                if(groceryLists.isNotEmpty()){
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        appBarMenuInfo.menus.forEach { menu ->
                            DropdownMenuItem(
                                onClick = {
                                    menu.onClick.invoke()
                                    menuExpanded = false
                                },
                                text = { Text(text = menu.title) }
                            )
                        }
                    }
                }


                if (cardInfo.canFavorite) {
                    IconButton(onClick = {
                        if (cardInfo.groceryItem.isFavorite > 0) {
                            cardInfo.viewModel.upsertGroceryItem(
                                GroceryItem(
                                    id = cardInfo.groceryItem.id,
                                    name = cardInfo.groceryItem.name,
                                    description = cardInfo.groceryItem.description,
                                    cardInfo.groceryItem.categoryId,
                                    isFavorite = 0,
                                    cardInfo.groceryItem.imagePath
                                )
                            )
                        } else {
                            cardInfo.viewModel.upsertGroceryItem(
                                GroceryItem(
                                    id = cardInfo.groceryItem.id,
                                    name = cardInfo.groceryItem.name,
                                    description = cardInfo.groceryItem.description,
                                    cardInfo.groceryItem.categoryId,
                                    isFavorite = 1,
                                    cardInfo.groceryItem.imagePath
                                )
                            )
                        }
                    }) {
                        Icon(
                            imageVector = if (cardInfo.groceryItem.isFavorite > 0) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = "Favorite",
                            tint = if (cardInfo.groceryItem.isFavorite > 0) Color.Red else Color.Black,
                        )
                    }
                }
                if (cardInfo.canDelete) {
                    IconButton(onClick = {
                        showDeleteDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer l'article ${cardInfo.groceryItem.name} ?") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cet article?") },
            confirmButton = {
                Button(
                    onClick = {
                        cardInfo.viewModel.deleteGroceryItem(cardInfo.groceryItem)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Oui")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Non")
                }
            }
        )
    }
}

