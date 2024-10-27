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
    suspend fun upsertGroceryItem(groceryItem: GroceryItem){
        groceryItemDao.upsertGroceryItem(groceryItem)
    }

    fun getFavoriteGroceryItems(): Flow<List<GroceryItem>> = groceryItemDao.getFavoriteGroceryItems()

    suspend fun deleteGroceryItem(groceryItem: GroceryItem){
        groceryItemDao.deleteGroceryItem(groceryItem)
    }

    fun getGroceryItemById(id: Long): Flow<GroceryItem>{
        return groceryItemDao.getGroceryItemById(id)
    }

    fun getAllGroceryItems(): Flow<List<GroceryItem>> = groceryItemDao.getAllGroceryItems()


    //Section pour le ListItemDao
    suspend fun upsertListItem(listItem: ListItem){
        listItemDao.upsertListItem(listItem)
    }

    suspend fun deleteListItem(listItem: ListItem){
        listItemDao.deleteListItem(listItem)
    }

    fun getListItemById(id: Long): Flow<ListItem>{
        return listItemDao.getListItemById(id)
    }


    //Section pour le CategoryDao
    suspend fun upsertCategory(category: Category){
        categoryDao.upsertCategory(category)
    }

    suspend fun deleteCategory(category: Category){
        categoryDao.deleteCategory(category)
    }

    fun getCategoryById(id: Long): Flow<Category>{
        return categoryDao.getCategoryById(id)
    }

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()


    //Section pour le GroceryListDao
    suspend fun upsertAGroceryList(groceryList: GroceryList){
        groceryListDao.upsertAGroceryList(groceryList)
    }

    suspend fun deleteGroceryList(groceryList: GroceryList){
        groceryListDao.deleteGroceryList(groceryList)
    }

    fun getGroceryListById(id: Long): Flow<GroceryList>{
        return groceryListDao.getGroceryListById(id)
    }

    fun getAllGroceryLists(): Flow<List<GroceryList>> = groceryListDao.getAllGroceryLists()
}