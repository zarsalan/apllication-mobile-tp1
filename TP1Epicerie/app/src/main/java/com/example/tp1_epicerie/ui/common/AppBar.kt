package com.example.tp1_epicerie.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen

// Composant réutilisable pour la barre d'application
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(
    title: String,
    navHostController: NavHostController,
    onBackNavClicked: () -> Unit = {},
    appBarMenuInfo: AppBarMenuInfo = AppBarMenuInfo(emptyList())
) {
    var menuExpanded by remember { mutableStateOf(false) }

    // Affichage de la flèche de retour si on n'est pas sur l'écran d'accueil
    val navigationIcon: (@Composable () -> Unit) = {
        if (!title.contains(Screen.HomeScreen.title())) {
            IconButton(onClick = {
                onBackNavClicked()
                if (navHostController.previousBackStackEntry != null) navHostController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
    }

    TopAppBar(
        title = { Text(text = title, color = colorResource(id = R.color.white)) },
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.app_bar),
        ),
        // Affichage du menu si des menus sont présents
        actions = {
            if (appBarMenuInfo.menus.isNotEmpty()) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Color.White
                    )
                }

                // Affichage des menus
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    appBarMenuInfo.menus.forEach { menu ->
                        DropdownMenuItem(
                            onClick = {
                                menu.onClick.invoke()
                                menuExpanded = false
                            },
                            text = { Text(text = menu.title) }
                        )
                    }
                }
            }
        }
    )
}

data class AppBarMenu(
    val title: String,
    val onClick: () -> Unit
)

data class AppBarMenuInfo(
    val menus: List<AppBarMenu> = emptyList()
)