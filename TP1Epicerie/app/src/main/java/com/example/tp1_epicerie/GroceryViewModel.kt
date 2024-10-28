package com.example.tp1_epicerie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1_epicerie.data.Category
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.GroceryList
import com.example.tp1_epicerie.data.GroceryRepository
import com.example.tp1_epicerie.data.ListItem
import com.example.tp1_epicerie.data.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class GroceryViewModel(
    private val groceryRepository: GroceryRepository = Graph.groceryRepository
): ViewModel() {
    //Section pour les GroceryItems -------------------------------------
    lateinit var getAllGroceryItems: Flow<List<GroceryItem>>
    init{
        viewModelScope.launch {
            getAllGroceryItems = groceryRepository.getAllGroceryItems()
        }
    }

    lateinit var getFavoriteGroceryItems: Flow<List<GroceryItem>>
    init{
        viewModelScope.launch {
            getFavoriteGroceryItems = groceryRepository.getFavoriteGroceryItems()
        }
    }

    fun upsertGroceryItem(groceryItem: GroceryItem){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertGroceryItem(groceryItem = groceryItem)
        }
    }

    fun updateGroceryItem(groceryItem: GroceryItem){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.updateGroceryItem(groceryItem = groceryItem)
        }
    }

    fun deleteGroceryItem(groceryItem: GroceryItem){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteGroceryItem(groceryItem = groceryItem)
        }
    }

    fun getGroceryItemById(id: Long): Flow<GroceryItem>{
        return groceryRepository.getGroceryItemById(id)
    }

    //Section pour les ListItem -------------------------------------
    fun upsertListItem(listItem: ListItem){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertListItem(listItem = listItem)
        }
    }

    fun deleteListItem(listItem: ListItem){
        viewModelScope.launch(Dispatchers.IO) {
           groceryRepository.deleteListItem(listItem = listItem)
        }
    }

    fun getListItemById(id: Long): Flow<ListItem>{
        return groceryRepository.getListItemById(id)
    }

    private val _listItem = MutableStateFlow<ListItem?>(null)
    val listItem: StateFlow<ListItem?> = _listItem

    fun fetchListItem(groceryListId: Long = 1L, groceryItemId: Long = 1L) {
        viewModelScope.launch {
            _listItem.value = getListItemByGroceryListId(groceryListId, groceryItemId).firstOrNull()
        }
    }

    private fun getListItemByGroceryListId(groceryListId: Long, groceryItemId: Long): Flow<ListItem?>{
        return groceryRepository.getListItemByGroceryListId(groceryListId, groceryItemId)
    }

    //Section pour les Categories
    fun upsertCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertCategory(category = category)
        }
    }

    fun updateCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.updateCategory(category = category)
        }
    }

    fun deleteCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteCategory(category = category)
        }
    }

    fun getCategoryById(id: Long): Flow<Category>{
        return groceryRepository.getCategoryById(id)
    }

    lateinit var getAllCategories: Flow<List<Category>>
    init{
        viewModelScope.launch {
            getAllCategories = groceryRepository.getAllCategories()
        }
    }


    //Section pour les GroceryLists
    lateinit var getAllGroceryLists: Flow<List<GroceryList>>
    init{
        viewModelScope.launch {
            getAllGroceryLists = groceryRepository.getAllGroceryLists()
        }
    }

    fun getGroceryListById(id: Long): Flow<GroceryList>{
        return groceryRepository.getGroceryListById(id)
    }

    fun upsertGroceryList(groceryList: GroceryList){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertAGroceryList(groceryList = groceryList)
        }
    }

    fun updateGroceryList(groceryList: GroceryList){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.updateGroceryList(groceryList = groceryList)
        }
    }

    fun deleteGroceryList(groceryList: GroceryList){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteGroceryList(groceryList = groceryList)
        }
    }


    //Section pour les Settings
    fun getSettings(): Flow<Settings?>{
        return groceryRepository.getSettings()
    }

    fun updateSettings(settings: Settings){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.updateSettings(settings = settings)
        }
    }

    fun upsertSettings(settings: Settings){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertSettings(settings = settings)
        }
    }
}