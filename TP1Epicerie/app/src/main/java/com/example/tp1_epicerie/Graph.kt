package com.example.tp1_epicerie

import android.content.Context
import androidx.room.Room
import com.example.tp1_epicerie.data.GroceryDatabase
import com.example.tp1_epicerie.data.GroceryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

object Graph {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private lateinit var database: GroceryDatabase

    // On initialise le repository avec les DAOs de la base de données
    val groceryRepository by lazy {
        GroceryRepository(
            groceryItemDao = database.groceryItemDao(),
            listItemDao = database.listItemDao(),
            categoryDao = database.categoryDao(),
            groceryListDao = database.groceryListDao(),
            settingsDao = database.settingsDao()
        )
    }

    // On initialise la base de données
    fun provide(context: Context) {
        database = GroceryDatabase.getDatabase(context, applicationScope)
    }
}