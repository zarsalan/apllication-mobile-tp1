package com.example.tp1_epicerie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeView(viewModel: GroceryViewModel, navController: NavController) {
    Scaffold(topBar = { AppBarView(title = Screen.HomeScreen.title) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.app_bar),
                onClick = {
                    navController.navigate(Screen.AddScreen.route)
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        val groceryList = viewModel.getAllGroceryLists.collectAsState(initial = emptyList())
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            item {
                CustomCard(
                    CardInfo(
                        "Tous les articles",
                        "Voir tous les articles",
                        { navController.navigate(Screen.AllItems.route) },
                        colorResource(id = R.color.all_items)
                    )
                )
            }
            item {
                CustomCard(
                    CardInfo(
                        "Favoris",
                        "Voir les articles favoris",
                        { navController.navigate(Screen.Favorites.route) },
                        colorResource(id = R.color.favorite_items)
                    )
                )
            }
            items(groceryList.value) { grocery ->
                CustomCard(
                    CardInfo(
                        grocery.title,
                        grocery.description,
                        { navController.navigate(Screen.GroceryList.route + "/${grocery.id}") },
                        Color.White
                    )
                )
            }
        }
    }
}

// Carte personnalisée
data class CardInfo(
    val title: String,
    val description: String,
    val onClick: () -> Unit,
    val containerColor: Color
)

@Composable
fun CustomCard(cardInfo: CardInfo) {
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
        Text(
            text = cardInfo.title,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}