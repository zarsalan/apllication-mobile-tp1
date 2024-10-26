package com.example.tp1_epicerie.data

import kotlinx.coroutines.flow.Flow

//Étant donné la petite taille de l'application nous avons centralisé les dao dans le repository
class GroceryRepository(
    private val groceryItemDao: GroceryItemDao,
    private val listItemDao: ListItemDao,
    private val categoryDao: CategoryDao,
    private val groceryListDao: GroceryListDao
){

    //Section pour le GroceryItemDao
    suspend fun upsertAGroceryItem(groceryItem: GroceryItem){
        groceryItemDao.upsertAGroceryItem(groceryItem)
    }

    fun getFavoriteGroceryItems(): Flow<List<GroceryItem>> = groceryItemDao.getFavoriteGroceryItems()

    suspend fun deleteAGroceryItem(groceryItem: GroceryItem){
        groceryItemDao.deleteAGroceryItem(groceryItem)
    }

    fun getAGroceryItemById(id: Int): Flow<GroceryItem>{
        return groceryItemDao.getAGroceryItemById(id)
    }

    fun getAllGroceryItems(): Flow<List<GroceryItem>> = groceryItemDao.getAllGroceryItems()


    //Section pour le ListItemDao
    suspend fun upsertAListItem(listItem: ListItem){
        listItemDao.upsertAListItem(listItem)
    }

    suspend fun deleteAListItem(listItem: ListItem){
        listItemDao.deleteAListItem(listItem)
    }

    fun getAListItemById(id: Int): Flow<ListItem>{
        return listItemDao.getAListItemById(id)
    }


    //Section pour le CategoryDao
    suspend fun upsertACategory(category: Category){
        categoryDao.upsertACategory(category)
    }

    suspend fun deleteACategory(category: Category){
        categoryDao.deleteACategory(category)
    }

    fun getACategoryById(id: Int): Flow<Category>{
        return categoryDao.getACategoryById(id)
    }


    //Section pour le GroceryListDao
    suspend fun upsertAGroceryList(groceryList: GroceryList){
        groceryListDao.upsertAGroceryList(groceryList)
    }

    suspend fun deleteAGroceryList(groceryList: GroceryList){
        groceryListDao.deleteAGroceryList(groceryList)
    }

    fun getAGroceryListById(id: Int): Flow<GroceryList>{
        return groceryListDao.getAGroceryListById(id)
    }

    fun getAllGroceryLists(): Flow<List<GroceryList>> = groceryListDao.getAllGroceryLists()
}