package com.example.tp1_epicerie.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(
    tableName = "groceryItem_table",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["groceryItem-category_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class GroceryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "groceryItem-name")
    val name: String = "",
    @ColumnInfo(name = "groceryItem-description")
    val description: String = "",
    @ColumnInfo(name = "groceryItem-category_id")
    val category_id: Int? = 0,
    @ColumnInfo(name = "groceryItem-isFavorite")
    val isFavorite: Int = 0,
    @ColumnInfo(name = "groceryItem-picture")
    val picture: String= ""
)

@Entity(
    tableName = "listItem-table",
    foreignKeys = [
        ForeignKey(
            entity = GroceryItem::class,
            parentColumns = ["id"],
            childColumns = ["listItem-item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "listItem-item_id")
    val item_id: Int = 0,
    @ColumnInfo(name = "listItem-quantity")
    val quantity: Int = 0,
    @ColumnInfo(name = "listItem-isCrossed")
    val isCrossed: Int = 0
)

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "category-title")
    val title: String = ""
)

@Entity(tableName = "groceryList_table")
data class GroceryList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "groceryList-title")
    val title: String = "",
    @ColumnInfo(name = "groceryList-description")
    val description: String = "",
    @ColumnInfo(name = "groceryList-listItem")
    val listItem: String = ""
)

class Converters {
    @TypeConverter
    fun fromList(list: List<Int>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(data: String?): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(data, listType)
    }
}