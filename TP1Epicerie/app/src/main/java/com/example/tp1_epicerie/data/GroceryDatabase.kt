package com.example.tp1_epicerie.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [GroceryItem::class, ListItem::class, Category::class, GroceryList::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GroceryDatabase : RoomDatabase(){
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
    }

    private class GroceryDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.categoryDao(), database.groceryItemDao(), database.listItemDao(), database.groceryListDao())
                }
            }
        }
        //Populer la bd
        suspend fun populateDatabase(categoryDao: CategoryDao, groceryItemDao: GroceryItemDao, listItemDao: ListItemDao, groceryListDao: GroceryListDao) {

            //Les catégories de base
            val categories = listOf(
                Category(id = 1, title = "Pain"),
                Category(id = 2, title = "Fruit"),
                Category(id = 3, title = "Légume"),
                Category(id = 4, title = "Produits Laitiers"),
                Category(id = 5, title = "Viande"),
                Category(id = 6, title = "Poisson"),
                Category(id = 7, title = "Epices"),
                Category(id = 8, title = "Boisson"),
                Category(id = 9, title = "Collations"),
                Category(id = 10, title = "Desserts"),
                Category(id = 11, title = "Céréales"),
                Category(id = 12, title = "Pâtes"),
                Category(id = 13, title = "Sauces"),
                Category(id = 14, title = "Conserves"),
                Category(id = 15, title = "Charcuterie"),
                Category(id = 16, title = "Huile"),
                Category(id = 17, title = "Noix"),
                Category(id = 18, title = "Café"),
                Category(id = 19, title = "Légumineuses"),
                Category(id = 20, title = "Produits Congelés"),
                Category(id = 21, title = "Pâtisserie")
            )

            categories.forEach { category ->
                categoryDao.upsertACategory(category)
            }


            //Les groceryItems de base
            val groceryItems = listOf(
                GroceryItem(id = 1, name = "Baguette", description = "Pain français", category_id = 1, isFavorite = 1, picture = null),
                GroceryItem(id = 2, name = "Banane", description = "Fruit tropical", category_id = 2, isFavorite = 0, picture = null),
                GroceryItem(id = 3, name = "Carotte", description = "Légume frais riche en vitamines", category_id = 3, isFavorite = 1, picture = null),
                GroceryItem(id = 4, name = "Yaourt nature", description = "Produit laitier sans sucre", category_id = 4, isFavorite = 0, picture = null),
                GroceryItem(id = 5, name = "Steak de boeuf", description = "Viande rouge", category_id = 5, isFavorite = 0, picture = null),
                GroceryItem(id = 6, name = "Saumon fumé", description = "Poisson riche en oméga-3", category_id = 6, isFavorite = 1, picture = null),
                GroceryItem(id = 7, name = "Poivre noir", description = "Epice pour relever le goût", category_id = 7, isFavorite = 0, picture = null),
                GroceryItem(id = 8, name = "Jus d'orange", description = "Boisson vitaminée sans sucre ajouté", category_id = 8, isFavorite = 1, picture = null),
                GroceryItem(id = 9, name = "Chips", description = "Collation croustillante salée", category_id = 9, isFavorite = 0, picture = null),
                GroceryItem(id = 10, name = "Tarte aux pommes", description = "Dessert pâtissier aux pommes", category_id = 10, isFavorite = 1, picture = null)
            )

            groceryItems.forEach { groceryItem ->
                groceryItemDao.upsertAGroceryItem(groceryItem)
            }


            //Les listItems de base
            val listItems = listOf(
                ListItem(id = 1, item_id = 1, quantity = 2, isCrossed = 0),
                ListItem(id = 2, item_id = 3, quantity = 8, isCrossed = 0),
                ListItem(id = 3, item_id = 5, quantity = 1, isCrossed = 1),
                ListItem(id = 4, item_id = 9, quantity = 1, isCrossed = 0)
            )

            listItems.forEach{ listItem ->
                listItemDao.upsertAListItem(listItem)
            }


            //La GroceryList de base
            val listItemIds = listItems.map{ it.id }

            val groceryList = GroceryList(
                id = 1,
                title = "Liste d'exemple",
                description = "Liste d'exemple",
                listItems = listItemIds
            )

            groceryListDao.upsertAGroceryList(groceryList)
        }
    }
}