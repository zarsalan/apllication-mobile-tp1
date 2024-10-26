package com.example.tp1_epicerie

import android.content.Context
import androidx.room.Room
import com.example.tp1_epicerie.data.GroceryDatabase
import com.example.tp1_epicerie.data.GroceryRepository

object Graph {
    lateinit var  database: GroceryDatabase

    val groceryRepository by lazy{
        GroceryRepository(
            groceryItemDao = database.groceryItemDao(),
            listItemDao = database.listItemDao(),
            categoryDao = database.categoryDao(),
            groceryListDao = database.groceryListDao()
        )
    }

    fun provide(context: Context){
        database = Room.databaseBuilder(context, GroceryDatabase::class.java, "grocery.db").build()
    }
}