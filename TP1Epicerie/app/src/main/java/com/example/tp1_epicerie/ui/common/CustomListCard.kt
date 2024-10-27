package com.example.tp1_epicerie.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.data.GroceryList

// Carte personnalisée
data class CustomListCardInfo(
    val listId: Long = 0L,
    val title: String,
    val description: String,
    val onClick: () -> Unit,
    val containerColor: Color,
    val canEdit: Boolean = false,
    val canDelete: Boolean = false,
    val groceryList: GroceryList? = null
)

@Composable
fun CustomListCard(viewModel: GroceryViewModel = viewModel(), cardInfo: CustomListCardInfo) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 4.dp, end = 8.dp)
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
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = cardInfo.title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Text(text = cardInfo.description)
            }

            if (cardInfo.canDelete || cardInfo.canEdit) {
                Row {
                    if (cardInfo.canEdit) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Icon",
                                tint = Color.Black
                            )
                        }
                    }
                    if (cardInfo.canDelete) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Icon",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog de suppression
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer la liste") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cette liste?") },
            confirmButton = {
                Button(
                    onClick = {
                        cardInfo.groceryList?.let {
                            viewModel.deleteGroceryList(it)
                        }
                        showDeleteDialog = false
                    }) {
                    Text("Oui")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                    }) {
                    Text("Non")
                }
            }
        )
    }
}