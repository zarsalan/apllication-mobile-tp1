package com.example.applimobile_tp1

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity
data class Element(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    val nom: String,
    val quantite: Int,
    val categorie: Categorie,
    val note: String
)

@Dao
interface ElementDao {
    @Query("SELECT * FROM Element")
    suspend fun getAll(): List<Element>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(element: Element)
}

@Database(entities = [Element::class], version = 1, exportSchema = false)
abstract class ElementDatabase: RoomDatabase() {

    abstract fun elementDao(): ElementDao

    companion object {
        @Volatile
        private var INSTANCE: ElementDatabase? = null

        fun getDatabase(ctx: Context): ElementDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    ElementDatabase::class.java,
                    "client_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
