package com.example.tp1_epicerie

sealed class Screen(val route: String, val title: String) {
    data object HomeScreen: Screen("home_screen", "Mes listes d'articles d'épicerie") // Affichage de tous les listes
    data object AddScreen: Screen("add_screen", "Ajouter une liste d'articles d'épicerie") // Ajout d'une liste

    data object ItemsList: Screen("items_list", "Les articles") // Affichage des items d'épicerie de base
    data object AddItem: Screen("add_item", "Ajouter un article") // Ajout d'un item d'épicerie de base
    data object EditItem: Screen("edit_item", "Éditer un article") // Modification d'un item d'épicerie de base

    data object Favorites: Screen("favorites", "Articles favoris") // Affichage des items favoris

    data object GroceryList: Screen("grocery_list", "Ma liste d'épicerie") // Affichage des items ajoutés dans une liste custom
    data object CreateGroceryList: Screen("create_grocery_list", "Créer une liste d'épicerie") // Création d'une liste custom
    data object AddToGroceryList: Screen("add_to_grocery_list", "Ajouter à une liste d'épicerie") // Ajout des items dans une liste custom (Affiché à la suite de GroceryList)
}