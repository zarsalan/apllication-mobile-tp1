package com.example.tp1_epicerie

import android.content.Context
import androidx.room.Room
import com.example.tp1_epicerie.data.GroceryDatabase
import com.example.tp1_epicerie.data.GroceryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

object Graph {
    private val applicationScope = CoroutineScope(SupervisorJob())

    lateinit var database: GroceryDatabase

    val groceryRepository by lazy {
        GroceryRepository(
            groceryItemDao = database.groceryItemDao(),
            listItemDao = database.listItemDao(),
            categoryDao = database.categoryDao(),
            groceryListDao = database.groceryListDao()
        )
    }

    fun provide(context: Context) {
        database = GroceryDatabase.getDatabase(context, applicationScope)
    }
}