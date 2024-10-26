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
    private val groceryRepository: GroceryRepository
): ViewModel() {

    //Section pour les GroceryItems
    fun upsertAGroceryItem(groceryItem: GroceryItem){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertAGroceryItem(groceryItem = groceryItem)
        }
    }

    lateinit var getFavoriteGroceryItems: Flow<List<GroceryItem>>

    init{
        viewModelScope.launch {
            getFavoriteGroceryItems = groceryRepository.getFavoriteGroceryItems()
        }
    }

    fun deleteAGroceryItem(groceryItem: GroceryItem){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteAGroceryItem(groceryItem = groceryItem)
        }
    }

    fun getAGroceryItemById(id: Int): Flow<GroceryItem>{
        return groceryRepository.getAGroceryItemById(id)
    }

    lateinit var getAllGroceryItems: Flow<List<GroceryItem>>

    init{
        viewModelScope.launch {
            getAllGroceryItems = groceryRepository.getAllGroceryItems()
        }
    }


    //Section pour les ListItem
    fun upsertAListItem(listItem: ListItem){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertAListItem(listItem = listItem)
        }
    }

    fun deleteAListItem(listItem: ListItem){
        viewModelScope.launch(Dispatchers.IO) {
           groceryRepository.deleteAListItem(listItem = listItem)
        }
    }

    fun getAListItemById(id: Int): Flow<ListItem>{
        return groceryRepository.getAListItemById(id)
    }

    //Section pour les Categories
    fun upsertACategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertACategory(category = category)
        }
    }

    fun deleteACategory(category: Category){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteACategory(category = category)
        }
    }

    fun getACategoryById(id: Int): Flow<Category>{
        return groceryRepository.getACategoryById(id)
    }


    //Section pour les GroceryLists
    lateinit var getAllGroceryLists: Flow<List<GroceryList>>

    init{
        viewModelScope.launch {
            getAllGroceryLists = groceryRepository.getAllGroceryLists()
        }
    }

    fun getAGroceryListById(id: Int): Flow<GroceryList>{
        return groceryRepository.getAGroceryListById(id)
    }

    fun upsertGroceryList(groceryList: GroceryList){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertAGroceryList(groceryList = groceryList)
        }
    }

    fun deleteAGroceryList(groceryList: GroceryList){
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteAGroceryList(groceryList = groceryList)
        }
    }
}