package com.example.todoapp.util

open class Constants {
    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "todo_db"
        const val TODO_DELETE_ACTION = "UNDO"

        //args
        const val TODO_ID_ARG = "todoId"
        const val PURPOSE_ARG = "purpose"

        //snackbar msgs
        const val TODO_DELETE_MSG = "Todo deleted"
        const val TODO_TITLE_MSG = "Title can't be empty"
    }
}