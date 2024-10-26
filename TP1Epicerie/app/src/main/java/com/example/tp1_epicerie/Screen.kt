package com.example.tp1_epicerie

sealed class Screen(val route: String, val title: String) {
    data object HomeScreen: Screen("home_screen", "Page d'accueil") // Affichage de tous les listes

    data object ItemsList: Screen("items_list", "") // Affichage des items d'épicerie de base
    data object AddItem: Screen("add_item", "") // Ajout d'un item d'épicerie de base
    data object EditItem: Screen("edit_item", "") // Modification d'un item d'épicerie de base

    data object GroceryList: Screen("grocery_list", "") // Affichage des items ajoutés dans une liste custom
    data object CreateGroceryList: Screen("create_grocery_list", "") // Création d'une liste custom
    data object AddToGroceryList: Screen("add_to_grocery_list", "") // Ajout des items dans une liste custom (Affiché à la suite de GroceryList)
}