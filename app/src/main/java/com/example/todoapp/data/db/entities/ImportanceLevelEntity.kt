package com.example.todoapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "importance_levels")
data class ImportanceLevelEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "importance_name") var importanceName: String
)