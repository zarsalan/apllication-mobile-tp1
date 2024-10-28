package com.example.tp1_epicerie.ui.common

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.GroceryList
import com.example.tp1_epicerie.data.ListItem

data class GroceryItemCardInfo(
    val groceryItem: GroceryItem,
    val viewModel: GroceryViewModel,
    val onClick: () -> Unit,
    val containerColor: Color,
)

@Composable
fun GroceryItemCard(
    viewModel: GroceryViewModel,
    cardInfo: GroceryItemCardInfo
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showQuantityDialog by remember { mutableStateOf(false) }
    var selectedQuantity by remember { mutableIntStateOf(1) }
    var selectedGroceryList by remember { mutableStateOf(GroceryList()) }
    val currentContext = LocalContext.current

    var menuExpanded by remember { mutableStateOf(false) }
    val groceryLists =
        cardInfo.viewModel.getAllGroceryLists.collectAsState(initial = emptyList()).value


    val appBarMenuInfo: AppBarMenuInfo = AppBarMenuInfo(
        groceryLists.map { groceryList ->
            AppBarMenu(
                title = groceryList.title,
                onClick = {
                    selectedGroceryList = groceryList
                    showQuantityDialog = true
                    menuExpanded = false
                    viewModel.fetchListItem(selectedGroceryList.id, cardInfo.groceryItem.id)
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

                if (groceryLists.isNotEmpty()) {
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        appBarMenuInfo.menus.forEach { menu ->
                            DropdownMenuItem(
                                onClick = {
                                    menu.onClick.invoke()
                                },
                                text = { Text(text = menu.title) }
                            )
                        }
                    }
                }

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
    val textItemAdded: String = stringResource(R.string.text_itemAdded)
    // Dialogue pour sélectionner la quantité
    if (showQuantityDialog) {
        AlertDialog(
            onDismissRequest = { showQuantityDialog = false },
            title = { Text(stringResource(R.string.text_quantitySelection)) },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.widthIn(max = 200.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { if (selectedQuantity > 1) selectedQuantity-- }) {
                            Icon(
                                painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = "Decrease"
                            )
                        }
                        Text(
                            text = "$selectedQuantity",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { selectedQuantity++ }) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.listItem.let { it ->
                        val listItem = it.value

                        if (listItem != null) {
                            viewModel.upsertListItem(
                                listItem.copy(quantity = listItem.quantity + selectedQuantity)
                            )
                        } else {
                            viewModel.upsertListItem(
                                ListItem(
                                    groceryListId = selectedGroceryList.id,
                                    groceryItemId = cardInfo.groceryItem.id,
                                    quantity = selectedQuantity
                                )
                            )
                        }

                        Toast.makeText(
                            currentContext,
                            textItemAdded + selectedGroceryList.title,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    showQuantityDialog = false
                    selectedQuantity = 1
                }) {
                    Text(stringResource(R.string.text_add))
                }
            },
            dismissButton = {
                Button(onClick = {
                    showQuantityDialog = false
                    selectedQuantity = 1
                }) {
                    Text(stringResource(R.string.text_cancel))
                }
            }
        )
    }

    // Dialogue de suppression
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.text_removeItem) + cardInfo.groceryItem.name +" ?") },
            text = { Text(stringResource(R.string.text_deleteVerification)) },
            confirmButton = {
                Button(
                    onClick = {
                        cardInfo.viewModel.deleteGroceryItem(cardInfo.groceryItem)
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.text_yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.text_no))
                }
            }
        )
    }
}

