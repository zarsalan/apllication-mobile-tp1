package com.example.tp1_epicerie.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GroceryItemDao {
    //Pour avoir la liste de tous les éléments
    @Query("Select * from `groceryItem_table`")
    abstract fun getAllGroceryItems(): Flow<List<GroceryItem>>

    //Pour avoir la liste des éléments favories
    @Query("Select * from `groceryItem_table` WHERE `groceryItem_isFavorite` = 1")
    abstract fun getFavoriteGroceryItems(): Flow<List<GroceryItem>>

    @Query("Select * from `groceryItem_table` WHERE id=:id")
    abstract fun getGroceryItemById(id: Long): Flow<GroceryItem>

    @Upsert
    abstract suspend fun upsertGroceryItem(groceryItemEntity: GroceryItem)

    @Update
    abstract suspend fun updateGroceryItem(groceryItemEntity: GroceryItem)

    @Delete
    abstract suspend fun deleteGroceryItem(groceryItemEntity: GroceryItem)
}

@Dao
abstract class ListItemDao {
    @Query("Select * from `listItem_table` WHERE id=:id")
    abstract fun getListItemById(id: Long): Flow<ListItem>

    @Query("SELECT * FROM `listItem_table` WHERE `listItem_groceryList_id` = :groceryListId AND `listItem_grocery_item_id` = :groceryItemId LIMIT 1")
    abstract fun getListItemByGroceryListId(groceryListId: Long, groceryItemId: Long): Flow<ListItem?>

    @Upsert
    abstract suspend fun upsertListItem(listItemEntity: ListItem)

    @Delete
    abstract suspend fun deleteListItem(listItemEntity: ListItem)

    @Query("DELETE FROM `listItem_table` WHERE id=:id")
    abstract suspend fun deleteListItemById(id: Long)
}

@Dao
abstract class CategoryDao {
    @Query("Select * from `category_table` WHERE id=:id")
    abstract fun getCategoryById(id: Long): Flow<Category>

    @Upsert
    abstract suspend fun upsertCategory(categoryEntity: Category)

    @Update
    abstract suspend fun updateCategory(categoryEntity: Category)

    @Delete
    abstract suspend fun deleteCategory(categoryEntity: Category)

    //Pour avoir la liste de tous les éléments
    @Query("Select * from `category_table`")
    abstract fun getAllCategories(): Flow<List<Category>>
}

@Dao
abstract class GroceryListDao {
    //Pour avoir la liste de tous les éléments
    @Query("Select * from `groceryList_table`")
    abstract fun getAllGroceryLists(): Flow<List<GroceryList>>

    @Query("Select * from `groceryList_table` WHERE id=:id")
    abstract fun getGroceryListById(id: Long): Flow<GroceryList>

    @Query("Select * from `listItem_table` WHERE `listItem_groceryList_id` = :id")
    abstract fun getGroceryListItems(id: Long): Flow<List<ListItem>>

    @Upsert
    abstract suspend fun upsertAGroceryList(groceryListEntity: GroceryList)

    @Update
    abstract suspend fun updateGroceryList(groceryListEntity: GroceryList)

    @Delete
    abstract suspend fun deleteGroceryList(groceryListEntity: GroceryList)
}

@Dao
abstract class SettingsDao {
    @Query("Select * from `settings_table` WHERE id==1")
    abstract fun getSettings(): Flow<Settings?>

    @Upsert
    abstract suspend fun upsertSettings(settingsEntity: Settings)

    @Update
    abstract suspend fun updateSettings(settingsEntity: Settings)
}

