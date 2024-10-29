package com.example.tp1_epicerie

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale

class GroceryViewModel(

    private val groceryRepository: GroceryRepository = Graph.groceryRepository
) : ViewModel() {
    //Section pour les GroceryItems -----------------------------------------------------
    lateinit var getAllGroceryItems: Flow<List<GroceryItem>>
    init {
        viewModelScope.launch {
            getAllGroceryItems = groceryRepository.getAllGroceryItems()
        }
    }

    lateinit var getFavoriteGroceryItems: Flow<List<GroceryItem>>
    init {
        viewModelScope.launch {
            getFavoriteGroceryItems = groceryRepository.getFavoriteGroceryItems()
        }
    }

    fun favoriteGroceryItemById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val groceryItem = groceryRepository.getGroceryItemById(id).firstOrNull()
            groceryItem?.let {
                groceryRepository.updateGroceryItem(groceryItem.copy(isFavorite = if (groceryItem.isFavorite == 0) 1 else 0))
            }
        }
    }

    fun upsertGroceryItem(groceryItem: GroceryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertGroceryItem(groceryItem = groceryItem)
        }
    }

    fun updateGroceryItem(groceryItem: GroceryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.updateGroceryItem(groceryItem = groceryItem)
        }
    }

    fun deleteGroceryItem(groceryItem: GroceryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteGroceryItem(groceryItem = groceryItem)
        }
    }

    fun getGroceryItemById(id: Long): Flow<GroceryItem> {
        return groceryRepository.getGroceryItemById(id)
    }

    //Section pour les ListItem --------------------------------------------------------------------------
    fun upsertListItem(listItem: ListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertListItem(listItem = listItem)
        }
    }

    fun updateListItem(listItem: ListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.updateListItem(listItem = listItem)
        }
    }

    fun deleteListItem(listItem: ListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteListItem(listItem = listItem)
        }
    }

    fun getListItemById(id: Long): Flow<ListItem> {
        return groceryRepository.getListItemById(id)
    }

    private val _listItem = MutableStateFlow<ListItem?>(null)
    val listItem: StateFlow<ListItem?> = _listItem

    fun fetchListItem(groceryListId: Long = 1L, groceryItemId: Long = 1L) {
        viewModelScope.launch {
            _listItem.value = getListItemByGroceryListId(groceryListId, groceryItemId).firstOrNull()
        }
    }

    private val _uncrossedListItem = MutableStateFlow<ListItem?>(null)
    val uncrossedListItem: StateFlow<ListItem?> = _uncrossedListItem

    fun fetchUncrossedListItem(groceryListId: Long = 1L, groceryItemId: Long = 1L) {
        viewModelScope.launch {
            _uncrossedListItem.value = getListItemByGroceryListId(groceryListId, groceryItemId, true).firstOrNull()
        }
    }

    fun updateListItemCrossedState(listItem: ListItem) {
        viewModelScope.launch {
            updateListItemCrossed(listItem)
        }
    }

    // On coche ou décoche un item de la liste et enlève les duplicats
    private suspend fun updateListItemCrossed(listItem: ListItem){
        if (listItem.isCrossed == 1) {
            // On doit decocher l'item
            val uncrossedSameListItem = groceryRepository.getUncrossedListItemByGroceryListId(listItem.groceryListId, listItem.groceryItemId).firstOrNull()

            if (uncrossedSameListItem != null) {
                groceryRepository.updateListItem(uncrossedSameListItem.copy(quantity = uncrossedSameListItem.quantity + listItem.quantity))
                groceryRepository.deleteListItem(listItem)
            } else {
                groceryRepository.updateListItem(listItem.copy(isCrossed = 0))
            }
        }
        else {
            // On doit cocher l'item
            val crossedSameListItem = groceryRepository.getCrossedListItemsByGroceryListId(listItem.groceryListId, listItem.groceryItemId).firstOrNull()

            if (crossedSameListItem != null) {
                groceryRepository.updateListItem(crossedSameListItem.copy(quantity = crossedSameListItem.quantity + listItem.quantity))
                groceryRepository.deleteListItem(listItem)
            } else {
                groceryRepository.updateListItem(listItem.copy(isCrossed = 1))
            }
        }
    }

    // On obtient un ListItem qui est cochée ou non cochée
    private fun getListItemByGroceryListId(
        groceryListId: Long,
        groceryItemId: Long,
        uncrossed: Boolean = false
    ): Flow<ListItem?> {
        return if (uncrossed) {
            groceryRepository.getUncrossedListItemByGroceryListId(groceryListId, groceryItemId)
        } else {
            groceryRepository.getListItemByGroceryListId(groceryListId, groceryItemId)
        }
    }

    //Section pour les Categories -------------------------------------
    fun upsertCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertCategory(category = category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.updateCategory(category = category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteCategory(category = category)
        }
    }

    fun getCategoryById(id: Long): Flow<Category> {
        return groceryRepository.getCategoryById(id)
    }

    lateinit var getAllCategories: Flow<List<Category>>

    init {
        viewModelScope.launch {
            getAllCategories = groceryRepository.getAllCategories()
        }
    }

    //Section pour les GroceryLists -------------------------------------
    lateinit var getAllGroceryLists: Flow<List<GroceryList>>
    init {
        viewModelScope.launch {
            getAllGroceryLists = groceryRepository.getAllGroceryLists()
        }
    }

    fun getGroceryListById(id: Long): Flow<GroceryList> {
        return groceryRepository.getGroceryListById(id)
    }

    fun getGroceryListItems(id: Long): Flow<List<ListItem>> {
        return groceryRepository.getGroceryListItems(id)
    }

    fun upsertGroceryList(groceryList: GroceryList) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertAGroceryList(groceryList = groceryList)
        }
    }

    fun updateGroceryList(groceryList: GroceryList) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.updateGroceryList(groceryList = groceryList)
        }
    }

    fun deleteGroceryList(groceryList: GroceryList) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.deleteGroceryList(groceryList = groceryList)
        }
    }

    // Section pour les Settings -------------------------------------
    fun getSettings(): Flow<Settings?> {
        return groceryRepository.getSettings()
    }

    fun updateSettings(settings: Settings) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.updateSettings(settings = settings)
        }
    }

    fun upsertSettings(settings: Settings) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryRepository.upsertSettings(settings = settings)
        }
    }

    // Dark mode, on initialise et obtient la valeur _isDarkTheme ----
    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: State<Boolean> = _isDarkTheme

    init {
        viewModelScope.launch {
            groceryRepository.getSettings()
                .map { settings -> settings?.darkMode == 1 }
                .collect { isDark -> _isDarkTheme.value = isDark }
        }
    }

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            val currentSettings = groceryRepository.getSettings().first() ?: Settings()
            groceryRepository.updateSettings(
                currentSettings.copy(darkMode = if (enabled) 1 else 0)
            )
        }
    }
}