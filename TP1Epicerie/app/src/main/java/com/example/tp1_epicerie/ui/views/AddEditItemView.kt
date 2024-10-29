package com.example.tp1_epicerie.ui.views

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.ui.common.AppBarMenu
import com.example.tp1_epicerie.ui.common.AppBarMenuInfo
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomDropdownMenu
import com.example.tp1_epicerie.ui.common.CustomDropdownMenus
import com.example.tp1_epicerie.ui.common.CustomTextField
import com.example.tp1_epicerie.ui.common.CustomYesNoDialog
import com.example.tp1_epicerie.ui.theme.submitButtonColors

// La page pour ajouter ou modifier un item
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
    var showDeleteDialog by remember { mutableStateOf(false) }
    val currentContext = LocalContext.current

    val itemDeletedText = stringResource(R.string.text_itemDeleted)

    groceryItem?.let {
        name = it.name
        description = it.description
        categoryId = it.categoryId
        selectedCategory = categories.find { category -> category.id == it.categoryId }?.title ?: ""
        isFavorite = it.isFavorite == 1
        imageUri = it.imagePath?.let { imagePath -> Uri.parse(imagePath) }
    }

    // Launcher pour récupérer une image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                imageUri = it
                currentContext.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
    )

    // ScrollState pour le scroll vertical si l'écran est trop petit
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            AppBarView(
                title = if (id == 0L) Screen.AddEditItem.title() else Screen.AddEditItem.title2(),
                navHostController = navHostController,
                appBarMenuInfo = AppBarMenuInfo(
                    menus = if (id != 0L) {
                        listOf(
                            AppBarMenu(
                                title = stringResource(R.string.menu_delete_this_item),
                                onClick = { showDeleteDialog = true }
                            )
                        )
                    } else {
                        emptyList()
                    }
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .verticalScroll(scrollState),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Colonne pour afficher le nom et la description de l'item
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically)
            ) {
                CustomTextField(
                    stringResource(R.string.text_name),
                    labelColor = MaterialTheme.colorScheme.primary,
                    name,
                    onValueChanged = { newValue -> name = newValue })
                CustomTextField(
                    stringResource(R.string.text_description),
                    labelColor = MaterialTheme.colorScheme.primary,
                    description,
                    onValueChanged = { newValue -> description = newValue })
            }

            // Dropdown pour sélectionner la catégorie
            CustomDropdownMenu(
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 8.dp),
                label = stringResource(R.string.text_category) + ":",
                labelColor = MaterialTheme.colorScheme.primary,
                value = selectedCategory,
                customDropdownMenus = CustomDropdownMenus(
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

            // Row pour ajouter ou retirer l'item des favoris
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.addEdit_favorite),
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
                                        categoryId = categoryId,
                                        isFavorite = isFavorite.compareTo(false),
                                        imagePath = imageUri?.toString()
                                    )
                                )
                            }
                        },
                    tint = if (isFavorite) colorResource(id = R.color.app_bar) else MaterialTheme.colorScheme.primary
                )
            }

            // Colonne pour ajouter ou supprimer une image ainsi que le bouton pour sauvegarder l'item
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                // Boutton pour ajouter ou supprimer une image
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    Button(
                        onClick = { imagePickerLauncher.launch(arrayOf("image/*")) },
                    ) {
                        Text(text =  stringResource(R.string.text_selectImage))
                    }
                    if (imageUri != null) {
                        Button(
                            onClick = {
                                imageUri = null
                                if (groceryItem != null) {
                                    viewModel.updateGroceryItem(groceryItem.copy(imagePath = null))
                                }
                            },
                        ) {
                            Text(stringResource(R.string.text_deleteImage))
                        }
                    }
                }

                // Affichage de l'image
                imageUri?.let { uri ->
                    AsyncImage(
                        model = ImageRequest.Builder(currentContext)
                            .data(uri)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp),
                        placeholder = painterResource(R.drawable.baseline_image_24),
                        error = painterResource(R.drawable.baseline_broken_image_24)
                    )
                }

                // Boutton pour sauvegarder l'item
                val textAlert: String = stringResource(R.string.addItem_alert)
                val textItemSaved: String = stringResource(R.string.text_saveItem)
                Button(
                    colors = ButtonDefaults.submitButtonColors(),
                    onClick = {
                        if (name.isEmpty() || description.isEmpty() || categoryId == 0L) {
                            Toast.makeText(
                                currentContext,
                                textAlert,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        // Si l'item existe déjà, on le met à jour, sinon on l'ajoute
                        if (groceryItem != null) {
                            viewModel.updateGroceryItem(
                                GroceryItem(
                                    id = id,
                                    name = name.trim(),
                                    description = description.trim(),
                                    categoryId = categoryId,
                                    isFavorite = isFavorite.compareTo(false),
                                    imagePath = imageUri?.toString()
                                )
                            )
                        } else {
                            viewModel.upsertGroceryItem(
                                GroceryItem(
                                    name = name.trim(),
                                    description = description.trim(),
                                    categoryId = categoryId,
                                    isFavorite = isFavorite.compareTo(false),
                                    imagePath = imageUri?.toString()
                                )
                            )
                        }
                        Toast.makeText(currentContext, textItemSaved, Toast.LENGTH_SHORT)
                            .show()
                        navHostController.popBackStack()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.text_save),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }

    // Dialog de suppression
    CustomYesNoDialog(
        visible = showDeleteDialog,
        onDismissRequest = { showDeleteDialog = false },
        title = stringResource(R.string.text_removeItem) + " ${groceryItem?.name ?: name}?",
        message = stringResource(R.string.text_deleteVerification),
        onYesWithContext = { context ->
            if (groceryItem != null) {
                viewModel.deleteGroceryItem(groceryItem)
            }
            showDeleteDialog = false
            navHostController.popBackStack()
            Toast.makeText(
                context,
                itemDeletedText,
                Toast.LENGTH_SHORT
            ).show()
        },
        onNo = {
            showDeleteDialog = false
        },
    )
}