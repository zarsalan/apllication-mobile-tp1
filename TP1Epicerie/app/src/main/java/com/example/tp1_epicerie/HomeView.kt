package com.example.tp1_epicerie

import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tp1_epicerie.data.GroceryList

@Composable
fun HomeView(navController: NavController, viewModel: GroceryViewModel) {
    Scaffold(topBar = { AppBarView(title = Screen.HomeScreen.title) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.app_bar_color),
                onClick = {
                    navController.navigate(Screen.AddScreen.route + "/0L")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        val groceryList = viewModel.getAllGroceryLists.collectAsState(initial = emptyList())
        LazyColumn(modifier = Modifier.fillMaxSize().padding(it)){
            items(groceryList.value){ grocery ->
                GroceryCard(grocery, navController)
            }
        }
    }
}

@Composable
fun GroceryCard(grocery: GroceryList, navController: NavController){
    Card(
        modifier = Modifier.padding(8.dp),
        onClick = {
            navController.navigate(Screen.GroceryList.route + "/${grocery.id}")
        }
    ){
        Text(
            text = grocery.title,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}