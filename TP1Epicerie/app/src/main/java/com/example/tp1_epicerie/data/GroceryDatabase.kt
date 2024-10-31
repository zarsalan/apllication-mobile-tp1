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
    version = 33,
    exportSchema = false
)
abstract class GroceryDatabase : RoomDatabase() {
    abstract fun groceryItemDao(): GroceryItemDao
    abstract fun listItemDao(): ListItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun groceryListDao(): GroceryListDao
    abstract fun settingsDao(): SettingsDao

    // Initiation de la base de données
    companion object {
        @Volatile
        private var INSTANCE: GroceryDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GroceryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroceryDatabase::class.java,
                    "grocery_database3"
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
            // On intialise la base de données avec des données de base
            override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database)
                    }
                }
            }

            // On ajoute des données de base si la base de données est vide
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database)
                    }
                }
            }
        }

        suspend fun populateDatabase(database: GroceryDatabase) {
            val categoryDao = database.categoryDao()
            val groceryItemDao = database.groceryItemDao()
            val settingsDao = database.settingsDao()
            val groceryListDao = database.groceryListDao()

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

            val groceryLists = groceryListDao.getAllGroceryLists().first()
            if (groceryLists.isEmpty()) {
                populateGroceryLists(groceryListDao)
            }
        }

        // Population des catégories
        suspend fun populateCategories(categoryDao: CategoryDao) {
            val categories = listOf(
                Category(title = "Non défini"),
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

        // Population des items d'épicerie
        suspend fun populateGroceryItems(groceryItemDao: GroceryItemDao) {
            val groceryItems = listOf(
                GroceryItem(
                    name = "Baguette",
                    description = "Pain français",
                    categoryId = 2L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Banane",
                    description = "Fruit tropical",
                    categoryId = 3L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Carotte",
                    description = "Légume frais riche en vitamines",
                    categoryId = 4L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Yaourt nature",
                    description = "Produit laitier sans sucre",
                    categoryId = 5L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Steak de boeuf",
                    description = "Viande rouge",
                    categoryId = 6L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Saumon fumé",
                    description = "Poisson riche en oméga-3",
                    categoryId = 6L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Poivre noir",
                    description = "Epice pour relever le goût",
                    categoryId = 7L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Jus d'orange",
                    description = "Boisson vitaminée sans sucre ajouté",
                    categoryId = 8L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Chips",
                    description = "Collation croustillante salée",
                    categoryId = 9L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Tarte aux pommes",
                    description = "Dessert pâtissier aux pommes",
                    categoryId = 10L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Flocons d'avoine",
                    description = "Céréales riches en fibres",
                    categoryId = 11L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Spaghetti",
                    description = "Pâtes italiennes longues",
                    categoryId = 13L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Sauce tomate",
                    description = "Sauce à base de tomates",
                    categoryId = 14L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Thon en conserve",
                    description = "Poisson en conserve",
                    categoryId = 15L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Jambon",
                    description = "Charcuterie de porc",
                    categoryId = 16L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Huile d'olive",
                    description = "Huile de cuisson saine",
                    categoryId = 17L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Amandes",
                    description = "Noix croquantes riches en protéines",
                    categoryId = 18L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Café moulu",
                    description = "Boisson énergisante du matin",
                    categoryId = 19L,
                    isFavorite = 1,
                ),
                GroceryItem(
                    name = "Pois chiches",
                    description = "Légumineuse riche en protéines",
                    categoryId = 20L,
                    isFavorite = 0,
                ),
                GroceryItem(
                    name = "Petits pois surgelés",
                    description = "Légume congelé pour les plats",
                    categoryId = 21L,
                    isFavorite = 1,
                )
            )

            groceryItems.forEach { groceryItem ->
                groceryItemDao.upsertGroceryItem(groceryItem)
            }
        }

        // Population des paramètres
        suspend fun populateSettings(settingsDao: SettingsDao) {
            val settings = Settings(id = 1, darkMode = 0)
            settingsDao.upsertSettings(settings)
        }

        // Population des listes de courses
        suspend fun populateGroceryLists(groceryListDao: GroceryListDao) {
            val groceryLists = listOf(
                GroceryList(
                    title = "Liste de courses maison",
                    description = "Liste de courses pour la semaine"
                ),
                GroceryList(
                    title = "Liste de courses chalet",
                    description = "Liste de courses pour le chalet"
                ),
            )
            groceryLists.forEach { groceryList ->
                groceryListDao.upsertAGroceryList(groceryList)
            }
        }
    }
}