package com.example.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.util.Constants

@Database(
    entities = [Todo::class],
    version = Constants.DATABASE_VERSION
)
abstract class TodoDatabase : RoomDatabase() {

    abstract val dao: TodoDao
}