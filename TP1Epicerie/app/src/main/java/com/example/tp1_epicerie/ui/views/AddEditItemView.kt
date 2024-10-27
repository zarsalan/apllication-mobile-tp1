package com.example.tp1_epicerie.ui.views

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomDropdownMenu
import com.example.tp1_epicerie.ui.common.CustomDropdownMenus
import com.example.tp1_epicerie.ui.common.CustomTextField
import coil.compose.rememberImagePainter

@Composable
fun AddEditItemView(
    id: Long = 0L,
    viewModel: GroceryViewModel,
    navHostController: NavHostController
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var categoryId by remember { mutableLongStateOf(0L) }
    var selectedCategory by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val groceryItem = viewModel.getGroceryItemById(id)
        .takeIf { id != 0L }
        ?.collectAsState(GroceryItem())
        ?.value
    val categories = viewModel.getAllCategories.collectAsState(initial = emptyList()).value
    val currentContext = LocalContext.current

    groceryItem?.let {
        name = it.name
        description = it.description
        categoryId = it.categoryId!!
        selectedCategory = categories.find { category -> category.id == it.categoryId }?.title ?: ""
        isFavorite = it.isFavorite == 1
        imageUri = Uri.parse(it.imagePath)
    }

    // Launcher pour récupérer une image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri = uri
        }
    )

    Scaffold(
        topBar = {
            AppBarView(
                title = Screen.AddEditItem.title,
                onBackNavClicked = { navHostController.popBackStack() })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CustomTextField("Nom", name, onValueChanged = { newValue -> name = newValue })
                CustomTextField(
                    "Description",
                    description,
                    onValueChanged = { newValue -> description = newValue })
            }

            CustomDropdownMenu(
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 8.dp),
                label = "Catégorie:",
                value = selectedCategory,
                customDropdownMenus = CustomDropdownMenus(
                    containerColor = Color.Gray,
                    menus = categories.map { category ->
                        CustomDropdownMenu(
                            text = category.title,
                            onClick = {
                                categoryId = category.id
                                selectedCategory = category.title
                            }
                        )
                    })
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ajouter dans mes favoris:",
                    modifier = Modifier
                        .padding(end = 8.dp),
                    fontSize = 18.sp

                )
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(40.dp)
                        .clickable {
                            isFavorite = !isFavorite
                            if (groceryItem != null) {
                                viewModel.updateGroceryItem(
                                    GroceryItem(
                                        id = id,
                                        name = name.trim(),
                                        description = description.trim(),
                                        isFavorite = isFavorite.compareTo(false)
                                    )
                                )
                            }
                        },
                    tint = if (isFavorite) colorResource(id = R.color.app_bar) else androidx.compose.ui.graphics.Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth().padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                ) {
                    Text("Sélectionner une image")
                }

                imageUri?.let { uri ->
                    androidx.compose.foundation.Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }

                Button(
                    onClick = {
                        if (groceryItem != null) {
                            viewModel.updateGroceryItem(
                                GroceryItem(
                                    id = id,
                                    name = name.trim(),
                                    description = description.trim(),
                                    categoryId = categoryId,
                                    isFavorite = isFavorite.compareTo(false),
                                    imagePath = imageUri.toString()
                                )
                            )
                        } else {
                            viewModel.upsertGroceryItem(
                                GroceryItem(
                                    name = name.trim(),
                                    description = description.trim(),
                                    categoryId = categoryId,
                                    isFavorite = isFavorite.compareTo(false),
                                    imagePath = imageUri.toString()
                                )
                            )
                        }
                        Toast.makeText(currentContext, "Article enregistré", Toast.LENGTH_SHORT)
                            .show()
                        navHostController.popBackStack()
                    }
                ) {
                    Text("Enregistrer")
                }
            }
        }
    }
}