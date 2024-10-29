package com.example.tp1_epicerie.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.ListItem

data class ListItemCardInfo(
    val listItem: MutableState<ListItem>,
    val onClick: () -> Unit,
    val containerColor: Color
)

@Composable
fun ListItemCard(
    viewModel: GroceryViewModel,
    cardInfo: ListItemCardInfo
) {
    val listItem = cardInfo.listItem.value
    val groceryItem: GroceryItem = viewModel.getGroceryItemById(listItem.groceryItemId)
        .collectAsState(initial = GroceryItem()).value
    var showDeleteDialog by remember { mutableStateOf(false) }
    val currentContext = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp, start = 3.dp, end = 3.dp)
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
            Row(
                modifier = Modifier
                    .padding(
                        start = 15.dp,
                        end = 3.dp,
                        top = 1.dp,
                        bottom = 1.dp
                    )
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Colonne pour afficher le nom et la description de l'item
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = groceryItem.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = groceryItem.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Image de l'item
                if (groceryItem.imagePath != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(currentContext)
                            .data(groceryItem.imagePath)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 10.dp),
                        placeholder = painterResource(R.drawable.baseline_image_24),
                        error = painterResource(R.drawable.baseline_broken_image_24)
                    )
                }

                // Colonne pour afficher la quantité et les boutons de modification
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Colonne pour changer la quantité
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            0.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {
                            viewModel.updateListItem(listItem.copy(quantity = listItem.quantity + 1))
                        }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Add",
                                tint = Color.Black
                            )
                        }
                        Text(text = listItem.quantity.toString())
                        IconButton(onClick = {
                            if (listItem.quantity > 1) {
                                viewModel.updateListItem(listItem.copy(quantity = listItem.quantity - 1))
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Substract",
                                tint = Color.Black
                            )
                        }
                    }

                    // Icône pour marquer/cocher l'item
                    IconButton(onClick = {
                        viewModel.updateListItemCrossedState(listItem)
                    }) {
                        Icon(
                            imageVector = if (listItem.isCrossed > 0) Icons.Filled.CheckCircle else Icons.Filled.Check,
                            contentDescription = "Checkmark",
                            tint = colorResource(id = R.color.check_mark)
                        )
                    }

                    // Icône pour marquer l'item comme favori
                    IconButton(onClick = {
                        if (groceryItem.isFavorite > 0) {
                            viewModel.updateGroceryItem(groceryItem.copy(isFavorite = 0))
                        } else {
                            viewModel.updateGroceryItem(groceryItem.copy(isFavorite = 1))
                        }
                    }) {
                        Icon(
                            imageVector = if (groceryItem.isFavorite > 0) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = "Favorite",
                            tint = if (groceryItem.isFavorite > 0) Color.Red else Color.Black,
                        )
                    }

                    // Icône pour supprimer l'item
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

        // Dialog de suppression
        CustomYesNoDialog(
            visible = showDeleteDialog,
            onDismissRequest = { showDeleteDialog = false },
            title = stringResource(R.string.text_removeItem) + " ${groceryItem.name}?",
            message = stringResource(R.string.text_delete_from_list_verification),
            onYes = {
                viewModel.deleteListItem(listItem)
                showDeleteDialog = false
            },
            onNo = {
                showDeleteDialog = false
            },
        )
    }
}