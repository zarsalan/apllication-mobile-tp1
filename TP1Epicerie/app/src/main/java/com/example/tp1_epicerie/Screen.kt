package com.example.tp1_epicerie

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class Screen(val route: String) {

    @Composable
    open fun title(): String = ""
    @Composable
    open fun title2(): String = ""

    // Page d'accueil
    object HomeScreen : Screen("home_screen") {
        @Composable
        override fun title() = stringResource(R.string.home_screen_title)
    }

    object AddEditListScreen : Screen("add_edit_list_screen") {
        @Composable
        override fun title() = stringResource(R.string.add_list_title)
        @Composable
        override fun title2() = stringResource(R.string.edit_list_title)
    }

    // Page pours les articles d'épicerie de base
    object AllItems : Screen("all_items") {
        @Composable
        override fun title() = stringResource(R.string.all_items_title)
    }

    object AddEditItem : Screen("add_edit_item") {
        @Composable
        override fun title() = stringResource(R.string.menu_addItem)
        @Composable
        override fun title2() = stringResource(R.string.menu_updateItem)
    }

    // Page favoris
    object Favorites : Screen("favorites") {
        @Composable
        override fun title() = stringResource(R.string.favorite_item)
    }
    // Page pour les listes d'épicerie custom
    object GroceryList : Screen("grocery_list") {
        @Composable
        override fun title() = stringResource(R.string.menu_mylist)
    }

    object CreateGroceryList : Screen("create_grocery_list") {
        @Composable
        override fun title() = stringResource(R.string.create_list)
    }

    object AddToGroceryList : Screen("add_to_grocery_list") {
        @Composable
        override fun title() = stringResource(R.string.add_list)
    }

    // Page pour les catégories
    object AddEditCategory : Screen("add_edit_category") {
        @Composable
        override fun title() = stringResource(R.string.menu_addCategory)
        @Composable
        override fun title2() = stringResource(R.string.menu_modifyCategories)
    }

    // Affichage de tous les catégories
    object Categories : Screen("all_categories") {
        @Composable
        override fun title() = stringResource(R.string.text_categories)
    }

    // Page pour les paramètres
    object Settings : Screen("settings") {
        @Composable
        override fun title() = stringResource(R.string.menu_settings)
    }
}