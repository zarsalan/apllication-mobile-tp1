package com.example.tp1_epicerie

sealed class Screen(val route: String, val title: String) {
    // Page d'accueil
    data object HomeScreen :
        Screen("home_screen", "Mes listes d'articles d'épicerie") // Affichage de tous les listes

    data object AddListScreen :
        Screen("add_list_screen", "Ajouter une liste d'articles d'épicerie") // Ajout d'une liste

    // Page pours les articles d'épicerie de base
    data object AllItems :
        Screen("all_items", "Les articles") // Affichage des items d'épicerie de base

    data object AddItem :
        Screen("add_item", "Ajouter un article") // Ajout d'un item d'épicerie de base

    data object EditItem :
        Screen("edit_item", "Éditer un article") // Modification d'un item d'épicerie de base

    // Page favoris
    data object Favorites : Screen("favorites", "Articles favoris") // Affichage des items favoris

    // Page pour les listes d'épicerie custom
    data object GroceryList : Screen(
        "grocery_list",
        "Ma liste d'épicerie"
    ) // Affichage des items ajoutés dans une liste custom

    data object CreateGroceryList :
        Screen("create_grocery_list", "Créer une liste d'épicerie") // Création d'une liste custom

    data object AddToGroceryList : Screen(
        "add_to_grocery_list",
        "Ajouter à une liste d'épicerie"
    ) // Ajout des items dans une liste custom (Affiché à la suite de GroceryList)

    // Page pour les catégories
    data object CreateCategory :
        Screen("create_category", "Créer une catégorie") // Création d'une catégorie

    // Page pour les paramètres
    data object Settings :
        Screen("settings", "Paramètres") // Affichage des paramètres
}