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
        private var database: AppDatabase? = null

        fun getDatabaseInstance(context: Context): AppDatabase {
            return if (database == null) {
                synchronized(this) {
                    Room.databaseBuilder(context, AppDatabase::class.java, "todoitems.db")
                        .createFromAsset("database_db.db")
                        .build()
                }
            } else {
                database!!
            }
        }
    }
}
