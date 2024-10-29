package com.example.tp1_epicerie.data

import kotlinx.coroutines.flow.Flow

//Étant donné la petite taille de l'application nous avons centralisé les dao dans le repository
class GroceryRepository(
    private val groceryItemDao: GroceryItemDao,
    private val listItemDao: ListItemDao,
    private val categoryDao: CategoryDao,
    private val groceryListDao: GroceryListDao,
    private val settingsDao: SettingsDao
) {

    //Section pour le GroceryItemDao ----------------------------------------------
    suspend fun upsertGroceryItem(groceryItem: GroceryItem) {
        groceryItemDao.upsertGroceryItem(groceryItem)
    }

    suspend fun updateGroceryItem(groceryItem: GroceryItem) {
        groceryItemDao.updateGroceryItem(groceryItem)
    }

    fun getFavoriteGroceryItems(): Flow<List<GroceryItem>> =
        groceryItemDao.getFavoriteGroceryItems()

    suspend fun deleteGroceryItem(groceryItem: GroceryItem) {
        groceryItemDao.deleteGroceryItem(groceryItem)
    }

    fun getGroceryItemById(id: Long): Flow<GroceryItem> {
        return groceryItemDao.getGroceryItemById(id)
    }

    fun getAllGroceryItems(): Flow<List<GroceryItem>> = groceryItemDao.getAllGroceryItems()


    //Section pour le ListItemDao ----------------------------------------------
    suspend fun upsertListItem(listItem: ListItem) {
        listItemDao.upsertListItem(listItem)
    }

    suspend fun updateListItem(listItem: ListItem) {
        listItemDao.updateListItem(listItem)
    }

    suspend fun deleteListItem(listItem: ListItem) {
        listItemDao.deleteListItem(listItem)
    }

    fun getListItemById(id: Long): Flow<ListItem> {
        return listItemDao.getListItemById(id)
    }

    fun getListItemByGroceryListId(groceryListId: Long, groceryItemId: Long): Flow<ListItem?> {
        return listItemDao.getListItemByGroceryListId(groceryListId, groceryItemId)
    }

    fun getUncrossedListItemByGroceryListId(groceryListId: Long, groceryItemId: Long): Flow<ListItem> {
        return listItemDao.getUncrossedListItemByGroceryListId(groceryListId, groceryItemId)
    }

    fun getCrossedListItemsByGroceryListId(groceryListId: Long, groceryItemId: Long): Flow<ListItem> {
        return listItemDao.getCrossedListItemsByGroceryListId(groceryListId, groceryItemId)
    }

    //Section pour le CategoryDao ----------------------------------------------
    suspend fun upsertCategory(category: Category) {
        categoryDao.upsertCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    fun getCategoryById(id: Long): Flow<Category> {
        return categoryDao.getCategoryById(id)
    }

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()


    //Section pour le GroceryListDao ----------------------------------------------
    suspend fun upsertAGroceryList(groceryList: GroceryList) {
        groceryListDao.upsertAGroceryList(groceryList)
    }

    suspend fun updateGroceryList(groceryList: GroceryList) {
        groceryListDao.updateGroceryList(groceryList)
    }

    suspend fun deleteGroceryList(groceryList: GroceryList) {
        groceryListDao.deleteGroceryList(groceryList)
    }

    fun getGroceryListById(id: Long): Flow<GroceryList> {
        return groceryListDao.getGroceryListById(id)
    }

    fun getAllGroceryLists(): Flow<List<GroceryList>> = groceryListDao.getAllGroceryLists()

    fun getGroceryListItems(id: Long): Flow<List<ListItem>> = groceryListDao.getGroceryListItems(id)

    //Section pour les Settings ----------------------------------------------
    fun getSettings(): Flow<Settings?> = settingsDao.getSettings()

    suspend fun updateSettings(settings: Settings) {
        settingsDao.updateSettings(settings)
    }

    suspend fun upsertSettings(settings: Settings) {
        settingsDao.upsertSettings(settings)
    }
}