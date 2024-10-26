package com.example.tp1_epicerie.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.GroceryList
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomTextField

@Composable
fun AddListView(
    id: Int,
    viewModel: GroceryViewModel,
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppBarView(title = Screen.AddListScreen.title) }
    ) {
        Column(modifier = Modifier.padding(it).wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                label = "Titre",
                value = title,
                onValueChanged = { newValue ->
                    title = newValue
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                label = "Description",
                value = description,
                onValueChanged = { newValue ->
                    description = newValue
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick={
                viewModel.upsertGroceryList(GroceryList(title = title, description = description, listItems = null))
                navController.popBackStack()
            }){
                Text(
                    text = "Creér la liste",
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}


