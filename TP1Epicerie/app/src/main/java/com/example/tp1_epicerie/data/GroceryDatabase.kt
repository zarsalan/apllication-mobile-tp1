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
    version = 1,
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
                    populateDatabase(database.categoryDao())
                }
            }
        }

        suspend fun populateDatabase(categoryDao: CategoryDao) {
            val categories = listOf(
                Category(title = "Pain"),
                Category(title = "Fruit"),
                Category(title = "Légume"),
                Category(title = "Produits Laitiers"),
                Category(title = "Viande"),
                Category(title = "Poisson"),
                Category(title = "Epices"),
                Category(title = "Boisson"),
                Category(title = "Snacks"),
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
                categoryDao.upsertACategory(category)
            }
        }
    }
}