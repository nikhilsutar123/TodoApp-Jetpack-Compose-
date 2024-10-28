package com.example.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val title: String,
    val desc: String?,
    val isDone: Boolean,
    @PrimaryKey val id: Int? = null
)
