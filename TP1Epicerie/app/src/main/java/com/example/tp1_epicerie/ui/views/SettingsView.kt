package com.example.tp1_epicerie.ui.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tp1_epicerie.GroceryViewModel
import com.example.tp1_epicerie.R
import com.example.tp1_epicerie.Screen
import com.example.tp1_epicerie.data.Settings
import com.example.tp1_epicerie.ui.common.AppBarView
import com.example.tp1_epicerie.ui.common.CustomDropdownMenu
import com.example.tp1_epicerie.ui.common.CustomDropdownMenus

// La page pour les paramètres de l'application
@Composable
fun SettingsView(viewModel: GroceryViewModel, navHostController: NavHostController) {
    val context = LocalContext.current

    var darkMode by remember { mutableStateOf(false) }

    val textLanguageFr: String = stringResource(R.string.language_french)
    val textLanguageEng: String = stringResource(R.string.language_english)
    var language by remember { mutableStateOf(textLanguageFr) }
    val currentContext = LocalContext.current

    val settings = viewModel.getSettings().collectAsState(initial = Settings()).value ?: Settings()

    darkMode = settings.darkMode == 1
    language = settings.language

    Log.d("SettingsView", "darkMode: $darkMode, language: $language")

    Scaffold(topBar = {
        AppBarView(
            title = Screen.AddEditCategory.title(),
            navHostController = navHostController
        )
    }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(top = 15.dp, bottom = 15.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val textsettingUpdated: String = stringResource(R.string.settings_toast)
            /*
            CustomDropdownMenu(
                modifier = Modifier.padding(start = 25.dp, top = 10.dp, end = 25.dp),
                label = stringResource(R.string.text_language),
                value = language,
                customDropdownMenus = CustomDropdownMenus(
                    menus = listOf(
                        textLanguageFr,
                        textLanguageEng
                    ).map { it ->
                        CustomDropdownMenu(
                            text = it,
                            onClick = {
                                language = it
                                viewModel.upsertSettings(
                                    Settings(
                                        darkMode = if (darkMode) 1 else 0,
                                        language = language
                                    )
                                )

                                Toast.makeText(
                                    currentContext,
                                    textsettingUpdated,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                )
            )*/
            
            CustomDropdownMenu(
                modifier = Modifier.padding(start = 25.dp, top = 10.dp, end = 25.dp),
                label = stringResource(R.string.text_theme),
                labelColor = MaterialTheme.colorScheme.primary,
                value = if (darkMode) "Sombre" else "Clair",
                customDropdownMenus = CustomDropdownMenus(
                    menus = listOf(
                        "Clair",
                        "Sombre"
                    ).map { theme ->
                        CustomDropdownMenu(
                            text = theme,
                            onClick = {
                                darkMode = theme == "Sombre"
                                viewModel.upsertSettings(
                                    Settings(
                                        darkMode = if (theme == "Sombre") 1 else 0,
                                        language = language
                                    )
                                )
                                viewModel.updateDarkMode(if (theme == "Sombre") true else false)
                                Toast.makeText(
                                    currentContext,
                                    "Vos paramètres ont été mis à jour",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                )
            )
        }
    }
}