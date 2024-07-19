package com.example.todoapp.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.data.db.entities.ImportanceLevelEntity
import com.example.todoapp.data.db.entities.TodoItemEntity

@Database(
    version = 2,
    autoMigrations = [AutoMigration(1, 2)],
    entities = [
        ImportanceLevelEntity::class,
        TodoItemEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTodoItemDao(): TodoItemDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var database: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return database ?: synchronized(this) {
                database ?: buildDatabase(context).also { database = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "todoitems.db")
                .createFromAsset("database_db.db")
                .build()
        }
    }
}
