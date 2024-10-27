package com.example.tp1_epicerie.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [GroceryItem::class, ListItem::class, Category::class, GroceryList::class],
    version = 12,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GroceryDatabase : RoomDatabase() {
    abstract fun groceryItemDao(): GroceryItemDao
    abstract fun listItemDao(): ListItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun groceryListDao(): GroceryListDao

    companion object {
        @Volatile
        private var INSTANCE: GroceryDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GroceryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroceryDatabase::class.java,
                    "grocery_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(GroceryDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class GroceryDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateCategories(database.categoryDao())
                        populateGroceryItems(database.groceryItemDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        val categoryDao = database.categoryDao()
                        val groceryItemDao = database.groceryItemDao()

                        categoryDao.getAllCategories().collect(){
                            categories -> if (categories.isEmpty()) {
                                populateCategories(categoryDao)
                            }
                        }

                        groceryItemDao.getAllGroceryItems().collect(){
                            groceryItems -> if (groceryItems.isEmpty()) {
                                populateGroceryItems(groceryItemDao)
                            }
                        }
                    }
                }
            }
        }

        suspend fun populateCategories(categoryDao: CategoryDao) {
            val categories = listOf(
                Category(id = 0L, title = "Non défini"),
                Category(id = 1L, title = "Pain"),
                Category(id = 2L, title = "Fruit"),
                Category(id = 3L, title = "Légume"),
                Category(id = 4L, title = "Produits Laitiers"),
                Category(id = 5L, title = "Viande"),
                Category(id = 6L, title = "Poisson"),
                Category(id = 7L, title = "Epices"),
                Category(id = 8L, title = "Boisson"),
                Category(id = 9L, title = "Collations"),
                Category(id = 10L, title = "Desserts"),
                Category(id = 11L, title = "Céréales"),
                Category(id = 12L, title = "Pâtes"),
                Category(id = 13L, title = "Sauces"),
                Category(id = 14L, title = "Conserves"),
                Category(id = 15L, title = "Charcuterie"),
                Category(id = 16L, title = "Huile"),
                Category(id = 17L, title = "Noix"),
                Category(id = 18L, title = "Café"),
                Category(id = 19L, title = "Légumineuses"),
                Category(id = 20L, title = "Produits Congelés"),
                Category(id = 21L, title = "Pâtisserie")
            )

            categories.forEach { category ->
                categoryDao.upsertCategory(category)
            }
        }

        suspend fun populateGroceryItems(groceryItemDao: GroceryItemDao) {
            val groceryItems = listOf(
                GroceryItem(
                    id = 1L,
                    name = "Baguette",
                    description = "Pain français",
                    categoryId = 0L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    id = 2L,
                    name = "Banane",
                    description = "Fruit tropical",
                    categoryId = 1L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    id = 3L,
                    name = "Carotte",
                    description = "Légume frais riche en vitamines",
                    categoryId = 2L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    id = 4L,
                    name = "Yaourt nature",
                    description = "Produit laitier sans sucre",
                    categoryId = 3L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    id = 5L,
                    name = "Steak de boeuf",
                    description = "Viande rouge",
                    categoryId = 4L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    id = 6L,
                    name = "Saumon fumé",
                    description = "Poisson riche en oméga-3",
                    categoryId = 5L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    id = 7L,
                    name = "Poivre noir",
                    description = "Epice pour relever le goût",
                    categoryId = 6L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    id = 8L,
                    name = "Jus d'orange",
                    description = "Boisson vitaminée sans sucre ajouté",
                    categoryId = 7L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    id = 9L,
                    name = "Chips",
                    description = "Collation croustillante salée",
                    categoryId = 8L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    id = 10L,
                    name = "Tarte aux pommes",
                    description = "Dessert pâtissier aux pommes",
                    categoryId = 9L,
                    isFavorite = 1,
                )
            )

            groceryItems.forEach { groceryItem ->
                groceryItemDao.upsertGroceryItem(groceryItem)
            }
        }
    }
}