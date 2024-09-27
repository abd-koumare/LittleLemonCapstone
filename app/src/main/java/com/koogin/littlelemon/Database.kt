package com.koogin.littlelemon

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase


@Entity(tableName = "menu_item")
data class MenuItemRoom(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val price: String,
    val category: String,
    val image: String
)

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM menu_item")
    fun getAll(): LiveData<List<MenuItemRoom>>

    @Insert
    fun insertAll(vararg menuItems: MenuItemRoom)


    @Insert
    fun insert(menuItemRoom: MenuItemRoom)

    @Query("SELECT (SELECT COUNT(*) FROM menu_item) == 0")
    fun isEmpty(): Boolean
}

@Database(entities = [MenuItemRoom::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao

    companion object {

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            val tempInstance = INSTANCE

            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "littlelemon-db"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}