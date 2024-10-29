package com.example.tp1_epicerie.ui.common

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
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

// Composant de carte d'élément d'épicerie
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

    val itemDeletedText = stringResource(R.string.text_itemDeleted)

    val appBarMenuInfo: AppBarMenuInfo = AppBarMenuInfo(
        groceryLists.map { groceryList ->
            AppBarMenu(
                title = groceryList.title,
                onClick = {
                    selectedGroceryList = groceryList
                    showQuantityDialog = true
                    menuExpanded = false

                    // On récupère l'élément d'épicerie non barré correspondant à l'élément sélectionné
                    viewModel.fetchUncrossedListItem(selectedGroceryList.id, cardInfo.groceryItem.id)
                }
            )
        }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp, start = 6.dp, end = 6.dp)
            .clickable { cardInfo.onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardInfo.containerColor,
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Affichage du nom, de la description et de l'image de l'élément d'épicerie
            Row(
                modifier = Modifier
                    .padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .weight(1.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Affichage du nom et de la description de l'élément d'épicerie un par dessus l'autre
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = cardInfo.groceryItem.name,
                        fontWeight = FontWeight.ExtraBold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = cardInfo.groceryItem.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (cardInfo.groceryItem.imagePath != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(currentContext)
                            .data(Uri.parse(cardInfo.groceryItem.imagePath))
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterVertically),
                        placeholder = painterResource(R.drawable.baseline_image_24),
                        error = painterResource(R.drawable.baseline_broken_image_24),
                    )
                }
            }

            // Affichage des boutons d'ajout, de favoris et de suppression
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 5.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Bouton d'ajout
                IconButton(onClick = {
                    menuExpanded = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.Black
                    )
                }
                // On affiche le menu déroulant des listes d'épicerie
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

                // Bouton de favoris
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

                // Bouton de suppression
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
    // Dialogue pour sélectionner la quantité à ajouter
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
                    viewModel.uncrossedListItem.let { it ->
                        val listItem = it.value

                        // Si l'élément est déjà dans la liste, on incrémente la quantité
                        if (listItem != null) {
                            viewModel.upsertListItem(
                                listItem.copy(quantity = listItem.quantity + selectedQuantity)
                            )
                        } else {
                            // Sinon, on ajoute l'élément à la liste
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
                            textItemAdded + " ${selectedGroceryList.title}",
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

    // Dialog de suppression
    CustomYesNoDialog(
        visible = showDeleteDialog,
        onDismissRequest = { showDeleteDialog = false },
        title = stringResource(R.string.text_removeItem) + " ${cardInfo.groceryItem.name}?",
        message = stringResource(R.string.text_deleteVerification),
        onYesWithContext = {context ->
            cardInfo.viewModel.deleteGroceryItem(cardInfo.groceryItem)
            showDeleteDialog = false
            Toast.makeText(context, itemDeletedText, Toast.LENGTH_SHORT).show()
        },
        onNo = {
            showDeleteDialog = false
        },
    )
}

