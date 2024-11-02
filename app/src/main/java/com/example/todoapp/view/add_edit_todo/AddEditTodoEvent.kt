package com.example.todoapp.view.add_edit_todo

sealed class AddEditTodoEvent {
    data class OnTitleChange(val title:String) : AddEditTodoEvent()
    data class OnDescChange(val desc:String) : AddEditTodoEvent()
    data object OnSaveTodoClick : AddEditTodoEvent()
}