package com.example.tp1_epicerie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1_epicerie.data.Category
import com.example.tp1_epicerie.data.GroceryItem
import com.example.tp1_epicerie.data.GroceryList
import com.example.tp1_epicerie.data.GroceryRepository
import com.example.tp1_epicerie.data.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
}