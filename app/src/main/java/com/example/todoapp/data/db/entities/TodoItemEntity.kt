package com.example.todoapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.utils.getImportanceId

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
    var text: String,
    var deadline: Long?,
    var done: Boolean,
    val createdAt: Long,
    var changedAt: Long?,
    var isDeleted: Boolean,
)

fun TodoItem.toEntity(isDeleted: Boolean = false): TodoItemEntity =
    TodoItemEntity(
        id = this.id,
        importanceId = this.importance.getImportanceId(),
        text = this.text,
        deadline = this.deadline,
        done = this.isDone,
        createdAt = this.creationDate,
        changedAt = this.modificationDate,
        isDeleted = isDeleted
    )
