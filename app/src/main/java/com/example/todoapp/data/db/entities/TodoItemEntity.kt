package com.example.todoapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "todo",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = ImportanceLevelEntity::class,
            parentColumns = ["id"],
            childColumns = ["importance_id"]
        )
    ]
)
data class TodoItemEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "importance_id") var importanceId: Int,
    val text: String,
    val deadline: Long?,
    val done: Boolean,
    val createdAt: Long,
    val changedAt: Long?,
    val isDeleted: Boolean,
)
