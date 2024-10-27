package com.example.tp1_epicerie.ui.common

import androidx.compose.ui.graphics.Color
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.ListItem

data class GroceryItemCard(
    val groceryItem: GroceryItem,
    val onClick: () -> Unit,
    val containerColor: Color
)

