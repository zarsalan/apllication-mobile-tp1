package com.example.tp1_epicerie.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Entité pour les éléments de base l'épicerie
@Entity(
    tableName = "groceryItem_table",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["groceryItem_category_id"],
            onDelete = ForeignKey.SET_DEFAULT
        )
    ],
    indices = [Index(value = ["groceryItem_category_id"])]
)
data class GroceryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "groceryItem_name")
    val name: String = "",
    @ColumnInfo(name = "groceryItem_description")
    val description: String = "",
    @ColumnInfo(name = "groceryItem_category_id")
    val categoryId: Long = 0L,
    @ColumnInfo(name = "groceryItem_isFavorite")
    val isFavorite: Int = 0,
    @ColumnInfo(name = "groceryItem_picture")
    val imagePath: String?= null
)

// Entité pour les éléments à cocher de la liste d'épicerie
@Entity(
    tableName = "listItem_table",
    foreignKeys = [
        ForeignKey(
            entity = GroceryList::class,
            parentColumns = ["id"],
            childColumns = ["listItem_groceryList_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GroceryItem::class,
            parentColumns = ["id"],
            childColumns = ["listItem_grocery_item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["listItem_grocery_item_id"]), Index(value = ["listItem_groceryList_id"])]
)
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "listItem_grocery_item_id")
    val groceryItemId: Long = 0L,
    @ColumnInfo(name = "listItem_groceryList_id")
    val groceryListId: Long = 0L,
    @ColumnInfo(name = "listItem_quantity")
    val quantity: Int = 0,
    @ColumnInfo(name = "listItem_isCrossed")
    val isCrossed: Int = 0
)

// Entité pour les catégories
@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "category_title")
    val title: String = ""
)

// Entité pour les listes d'épicerie
@Entity(tableName = "groceryList_table")
data class GroceryList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "groceryList_title")
    val title: String = "",
    @ColumnInfo(name = "groceryList_description")
    val description: String = "",
)

// Entité pour les paramètres de l'application
@Entity(tableName = "settings_table")
data class Settings(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "dark_mode")
    val darkMode: Int = 0,
    @ColumnInfo(name = "language")
    val language: String = "fr"
)