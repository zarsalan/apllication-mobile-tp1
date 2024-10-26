package com.example.tp1_epicerie.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GroceryItemDao {

    @Upsert
    abstract suspend fun upsertAGroceryItem(groceryItemEntity: GroceryItem)

    //Pour avoir la liste des éléments favories
    @Query("Select * from `groceryItem_table` WHERE `groceryItem_isFavorite` = 1")
    abstract fun getFavoriteGroceryItems(): Flow<List<GroceryItem>>

    @Delete
    abstract suspend fun deleteAGroceryItem(groceryItemEntity: GroceryItem)

    @Query("Select * from `groceryItem_table` WHERE id=:id")
    abstract fun getAGroceryItemById(id: Int): Flow<GroceryItem>

    //Pour avoir la liste de tous les éléments
    @Query("Select * from `groceryItem_table`")
    abstract fun getAllGroceryItems(): Flow<List<GroceryItem>>
}

@Dao
abstract class ListItemDao {

    @Upsert
    abstract suspend fun upsertAListItem(listItemEntity: ListItem)

    @Delete
    abstract suspend fun deleteAListItem(listItemEntity: ListItem)

    @Query("Select * from `listItem_table` WHERE id=:id")
    abstract fun getAListItemById(id: Int): Flow<ListItem>
}

@Dao
abstract class CategoryDao {

    @Upsert
    abstract suspend fun upsertACategory(categoryEntity: Category)

    @Delete
    abstract suspend fun deleteACategory(categoryEntity: Category)

    @Query("Select * from `category_table` WHERE id=:id")
    abstract fun getACategoryById(id: Int): Flow<Category>

    //Pour avoir la liste de tous les éléments
    @Query("Select * from `category_table`")
    abstract fun getAllCategories(): Flow<List<Category>>
}

@Dao
abstract class GroceryListDao {

    @Upsert
    abstract suspend fun upsertAGroceryList(groceryListEntity: GroceryList)

    @Delete
    abstract suspend fun deleteAGroceryList(groceryListEntity: GroceryList)

    @Query("Select * from `groceryList_table` WHERE id=:id")
    abstract fun getAGroceryListById(id: Int): Flow<GroceryList>

    //Pour avoir la liste de tous les éléments
    @Query("Select * from `groceryList_table`")
    abstract fun getAllGroceryLists(): Flow<List<GroceryList>>
}

