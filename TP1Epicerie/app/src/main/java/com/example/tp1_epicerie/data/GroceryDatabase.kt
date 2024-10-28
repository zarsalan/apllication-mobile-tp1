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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Database(
    entities = [GroceryItem::class, ListItem::class, Category::class, GroceryList::class, Settings::class],
    version = 18,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GroceryDatabase : RoomDatabase() {
    abstract fun groceryItemDao(): GroceryItemDao
    abstract fun listItemDao(): ListItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun groceryListDao(): GroceryListDao
    abstract fun settingsDao(): SettingsDao

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
                        populateSettings(database.settingsDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        val categoryDao = database.categoryDao()
                        val groceryItemDao = database.groceryItemDao()
                        val settingsDao = database.settingsDao()

                        val settings = settingsDao.getSettings().firstOrNull()
                        if (settings == null) {
                            populateSettings(settingsDao)
                        }

                        val categories = categoryDao.getAllCategories().first()
                        if (categories.isEmpty()) {
                            populateCategories(categoryDao)
                        }

                        val groceryItems = groceryItemDao.getAllGroceryItems().first()
                        if (groceryItems.isEmpty()) {
                            populateGroceryItems(groceryItemDao)
                        }
                    }
                }
            }
        }

        suspend fun populateCategories(categoryDao: CategoryDao) {
            val categories = listOf(
                Category(id = 1L, title = "Non défini"),
                Category(title = "Pain"),
                Category(title = "Fruit"),
                Category(title = "Légume"),
                Category(title = "Produits Laitiers"),
                Category(title = "Viande"),
                Category(title = "Poisson"),
                Category(title = "Epices"),
                Category(title = "Boisson"),
                Category(title = "Collations"),
                Category(title = "Desserts"),
                Category(title = "Céréales"),
                Category(title = "Pâtes"),
                Category(title = "Sauces"),
                Category(title = "Conserves"),
                Category(title = "Charcuterie"),
                Category(title = "Huile"),
                Category(title = "Noix"),
                Category(title = "Café"),
                Category(title = "Légumineuses"),
                Category(title = "Produits Congelés"),
                Category(title = "Pâtisserie")
            )

            categories.forEach { category ->
                categoryDao.upsertCategory(category)
            }
        }

        suspend fun populateGroceryItems(groceryItemDao: GroceryItemDao) {
            val groceryItems = listOf(
                GroceryItem(
                    name = "Baguette",
                    description = "Pain français",
                    categoryId = 1L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Banane",
                    description = "Fruit tropical",
                    categoryId = 1L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Carotte",
                    description = "Légume frais riche en vitamines",
                    categoryId = 2L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Yaourt nature",
                    description = "Produit laitier sans sucre",
                    categoryId = 3L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Steak de boeuf",
                    description = "Viande rouge",
                    categoryId = 4L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Saumon fumé",
                    description = "Poisson riche en oméga-3",
                    categoryId = 5L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Poivre noir",
                    description = "Epice pour relever le goût",
                    categoryId = 6L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Jus d'orange",
                    description = "Boisson vitaminée sans sucre ajouté",
                    categoryId = 7L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Chips",
                    description = "Collation croustillante salée",
                    categoryId = 8L,
                    isFavorite = 0,
                ),
                GroceryItem(
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

        suspend fun populateSettings(settingsDao: SettingsDao) {
            val settings = Settings(id = 1, darkMode = 0)
            settingsDao.upsertSettings(settings)
        }
    }
}