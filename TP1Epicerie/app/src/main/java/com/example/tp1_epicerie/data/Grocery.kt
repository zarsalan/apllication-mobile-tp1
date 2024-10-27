package com.example.tp1_epicerie.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
            childColumns = ["groceryItem_category_id"],
            onDelete = ForeignKey.SET_NULL
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
    val categoryId: Long? = 0L,
    @ColumnInfo(name = "groceryItem_isFavorite")
    val isFavorite: Int = 0,
    @ColumnInfo(name = "groceryItem_picture")
    val picture: String?= null
)

@Entity(
    tableName = "listItem_table",
    foreignKeys = [
        ForeignKey(
            entity = GroceryItem::class,
            parentColumns = ["id"],
            childColumns = ["listItem_item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["listItem_item_id"])]
)
data class ListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "listItem_item_id")
    val itemId: Long = 0L,
    @ColumnInfo(name = "listItem_quantity")
    val quantity: Int = 0,
    @ColumnInfo(name = "listItem_isCrossed")
    val isCrossed: Int = 0
)

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "category_title")
    val title: String = ""
)

@Entity(tableName = "groceryList_table")
data class GroceryList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "groceryList_title")
    val title: String = "",
    @ColumnInfo(name = "groceryList_description")
    val description: String = "",
    @ColumnInfo(name = "groceryList_listItem")
    val listItems: List<Long>?//TypeConverter
)

// Permet la conversion de list pour le stockage dans SQLite
class Converters {
    @TypeConverter
    fun fromList(list: List<Long>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(data: String?): List<Long> {
        val listType = object : TypeToken<List<Long>>() {}.type
        return Gson().fromJson(data, listType)
    }
}