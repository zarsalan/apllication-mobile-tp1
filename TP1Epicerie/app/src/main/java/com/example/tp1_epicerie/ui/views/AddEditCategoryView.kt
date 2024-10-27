package com.example.tp1_epicerie.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.Category
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomTextField

@Composable
fun AddEditCategoryView(
    id: Long = 0L,
    viewModel: GroceryViewModel,
    navHostController: NavHostController
) {
    var title by remember { mutableStateOf("") }
    val category = viewModel.getCategoryById(id)
        .takeIf { id != 0L }
        ?.collectAsState(Category())
        ?.value

    title = ""

    category?.let {
        title = it.title
    }

    Scaffold(
        topBar = {
            AppBarView(
                title = if (category != null) Screen.AddEditCategory.title2 else Screen.AddEditCategory.title,
                onBackNavClicked = { navHostController.popBackStack() })
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(top = 15.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomTextField(
                label = "Titre",
                value = title,
                onValueChanged = { newValue ->
                    title = newValue
                }
            )

            if (id != 0L) {
                Button(modifier = Modifier.padding(top = 15.dp),
                    onClick = {
                        if (category != null) {
                            viewModel.updateCategory(
                                Category(
                                    id = category.id,
                                    title = title.trim(),
                                )
                            )
                            Toast.makeText(
                                navHostController.context,
                                "Catégorie mise à jour",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                navHostController.context,
                                "Catégorie introuvable",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        navHostController.popBackStack()
                    }) {
                    Text(
                        text = "Enregistrer",
                        style = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                }
            } else {
                Button(modifier = Modifier.padding(top = 15.dp),
                    onClick = {
                        viewModel.upsertCategory(
                            Category(
                                title = title.trim(),
                            )
                        )
                        Toast.makeText(
                            navHostController.context,
                            "Catégorie ajoutée",
                            Toast.LENGTH_SHORT
                        ).show()
                        navHostController.popBackStack()
                    }) {
                    Text(
                        text = "Ajouter",
                        style = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                }
            }
        }
    }
}