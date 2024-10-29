package com.example.tp1_epicerie.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
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
fun CustomListCard(
    viewModel: GroceryViewModel = viewModel(), navHostController: NavHostController,
    cardInfo: CustomListCardInfo
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
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
            // Titre et description
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(text = cardInfo.title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Text(text = cardInfo.description, overflow = TextOverflow.Ellipsis, maxLines = 1)
            }

            // Boutons d'édition et de suppression
            if (cardInfo.canDelete || cardInfo.canEdit) {
                Row(
                    modifier = Modifier
                        .padding(end = 5.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (cardInfo.canEdit) {
                        IconButton(onClick = {
                            navHostController.navigate(Screen.AddEditListScreen.route + "/${cardInfo.listId}")
                        }) {
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
    CustomYesNoDialog(
        visible = showDeleteDialog,
        onDismissRequest = { showDeleteDialog = false },
        title = stringResource(R.string.text_removeList) + " ${cardInfo.title}?",
        message = stringResource(R.string.text_listVerification),
        onYes = {
            cardInfo.groceryList?.let {
                viewModel.deleteGroceryList(it)
            }
            showDeleteDialog = false
        },
        onNo = {
            showDeleteDialog = false
        },
    )
}