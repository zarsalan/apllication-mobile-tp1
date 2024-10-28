package com.example.tp1_epicerie.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.ListItem

data class ListItemCardInfo(
    val listItem: ListItem,
    val viewModel: GroceryViewModel,
    val onClick: () -> Unit,
    val containerColor: Color
)

@Composable
fun ListItemCard(
    cardInfo: ListItemCardInfo
    ){
    val listItem = cardInfo.listItem
    val groceryItem: GroceryItem = cardInfo.viewModel.getGroceryItemById(listItem.groceryItemId)
        .collectAsState(initial = GroceryItem(0L, "", "", 0L, 0, null)).value

    val quantity by remember { mutableIntStateOf(listItem.quantity) }

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
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = groceryItem.name, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text(text = groceryItem.description)
            }
            Row(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = quantity.toString())
                if(groceryItem.imagePath != null){
                    androidx.compose.foundation.Image(
                        painter = rememberAsyncImagePainter(groceryItem.imagePath),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp,
                    Alignment.CenterVertically)
                ) {
                    IconButton(onClick = {
                        cardInfo.viewModel.upsertListItem(ListItem(id = listItem.id, groceryItemId = listItem.groceryItemId,  quantity = listItem.quantity + 1, isCrossed = listItem.isCrossed))
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Add",
                            tint = Color.Black
                        )
                    }
                    IconButton(onClick = {
                        if(listItem.quantity > 1){
                            cardInfo.viewModel.upsertListItem(ListItem(id = listItem.id, groceryItemId = listItem.groceryItemId,  quantity = listItem.quantity + 1, isCrossed = listItem.isCrossed))
                        }else{
                            cardInfo.viewModel.deleteListItem(listItem)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Substract",
                            tint = Color.Black
                        )
                    }
                }
                IconButton(onClick = {
                    if(listItem.isCrossed > 0){
                        cardInfo.viewModel.upsertListItem(ListItem(id = listItem.id, groceryItemId = listItem.groceryItemId,  quantity = listItem.quantity, isCrossed = 0))
                    }else{
                        cardInfo.viewModel.upsertListItem(ListItem(id = listItem.id, groceryItemId = listItem.groceryItemId,  quantity = listItem.quantity, isCrossed = 1))
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Checkmark",
                        tint = Color.Black
                    )
                }

                IconButton(onClick = {
                    if(groceryItem.isFavorite > 0){
                        cardInfo.viewModel.upsertGroceryItem(GroceryItem(id = groceryItem.id, name = groceryItem.name, description = groceryItem.description, groceryItem.categoryId, isFavorite = 0, groceryItem.imagePath))
                    }else{
                        cardInfo.viewModel.upsertGroceryItem(GroceryItem(id = groceryItem.id, name = groceryItem.name, description = groceryItem.description, groceryItem.categoryId, isFavorite = 1, groceryItem.imagePath))
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
                IconButton(onClick = {
                    cardInfo.viewModel.deleteListItem(listItem)
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