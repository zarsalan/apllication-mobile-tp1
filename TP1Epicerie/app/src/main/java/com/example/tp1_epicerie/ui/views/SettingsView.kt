package com.example.tp1_epicerie.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.Settings
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomDropdownMenu
import com.example.tp1_epicerie.ui.common.CustomDropdownMenus

@Composable
fun SettingsView(viewModel: GroceryViewModel, navHostController: NavHostController) {
    var darkMode by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("Français") }

    val settings = viewModel.getSettings().collectAsState(initial = Settings()).value ?: Settings()

    darkMode = settings.darkMode == 1
    language = settings.language

    Scaffold(topBar = {
        AppBarView(
            title = Screen.AddEditCategory.title,
            onBackNavClicked = { navHostController.popBackStack() })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(top = 15.dp, bottom = 15.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomDropdownMenu(
                modifier = Modifier.padding(start = 25.dp, top = 10.dp, end = 25.dp),
                label = "Langue",
                value = "",
                customDropdownMenus = CustomDropdownMenus(
                    menus = listOf(
                        "Français",
                        "English"
                    ).map { language ->
                        CustomDropdownMenu(
                            text = language,
                            onClick = {}
                        )
                    },
                )
            )

            CustomDropdownMenu(
                modifier = Modifier.padding(start = 25.dp, top = 10.dp, end = 25.dp),
                label = "Thème",
                value = "",
                customDropdownMenus = CustomDropdownMenus(
                    menus = listOf(
                        "Clair",
                        "Sombre"
                    ).map { theme ->
                        CustomDropdownMenu(
                            text = theme,
                            onClick = {}
                        )
                    },
                )
            )
        }
    }
}