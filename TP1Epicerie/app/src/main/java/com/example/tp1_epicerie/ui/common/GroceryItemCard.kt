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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.data.GroceryItem

data class GroceryItemCardInfo(
    val groceryItem: GroceryItem,
    val viewModel: GroceryViewModel,
    val onClick: () -> Unit,
    val containerColor: Color,
    val canFavorite: Boolean = false,
    val canDelete: Boolean = false,

    )

@Composable
fun GroceryItemCard(
    cardInfo: GroceryItemCardInfo
){

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
                Text(text = cardInfo.groceryItem.name, fontWeight = FontWeight.ExtraBold)
                Text(text = cardInfo.groceryItem.description)
            }
            Row(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(cardInfo.groceryItem.imagePath != null){
                    //TODO
                }
                IconButton(onClick = {
                    //TODO
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.Black
                    )
                }

                if(cardInfo.canFavorite){
                    IconButton(onClick = {
                        if(cardInfo.groceryItem.isFavorite > 0){
                            cardInfo.viewModel.upsertGroceryItem(GroceryItem(id = cardInfo.groceryItem.id, name = cardInfo.groceryItem.name, description = cardInfo.groceryItem.description, cardInfo.groceryItem.categoryId, isFavorite = 0, cardInfo.groceryItem.imagePath))
                        }else{
                            cardInfo.viewModel.upsertGroceryItem(GroceryItem(id = cardInfo.groceryItem.id, name = cardInfo.groceryItem.name, description = cardInfo.groceryItem.description, cardInfo.groceryItem.categoryId, isFavorite = 1, cardInfo.groceryItem.imagePath))
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
                if(cardInfo.canDelete){
                    IconButton(onClick = {
                        cardInfo.viewModel.deleteGroceryItem(cardInfo.groceryItem)
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
}

