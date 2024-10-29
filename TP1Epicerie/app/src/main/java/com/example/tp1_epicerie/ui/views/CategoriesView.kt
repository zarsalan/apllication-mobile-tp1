package com.example.tp1_epicerie.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomCategoryCard
import com.example.tp1_epicerie.ui.common.CustomCategoryCardInfo

// La page pour afficher les catégories
@Composable
fun CategoriesView(viewModel: GroceryViewModel, navHostController: NavHostController) {
    Scaffold(
        topBar = {
            AppBarView(
                title = Screen.Categories.title(),
                navHostController = navHostController,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.app_bar),
                onClick = {
                    navHostController.navigate(Screen.AddEditCategory.route + "/0L")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        val categoriesList = viewModel.getAllCategories.collectAsState(listOf()).value

        // Affichage des catégories dans un LazyColumn (comme recyclerView mais bien meilleur)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it).padding(top = 6.dp)
        ) {
            items(categoriesList) { category ->
                CustomCategoryCard(
                    viewModel = viewModel,
                    navHostController = navHostController,
                    cardInfo = CustomCategoryCardInfo(
                        categoryId = category.id,
                        category = category,
                        title = category.title,
                        onClick = { navHostController.navigate(Screen.AddEditCategory.route + "/${category.id}") },
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}