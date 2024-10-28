package com.example.todoapp.view.todolistscreen

import com.example.todoapp.data.Todo

sealed class TodoListEvent {
    data class OnDeleteTodoClick(val todo: Todo): TodoListEvent()
    object OnUndoDeleteClick: TodoListEvent()
    data class OnTodoClick(val todo:Todo): TodoListEvent()
    data class OnDoneChange(val todo: Todo, val isDone:Boolean):TodoListEvent()
    object OnAddTodoClick:TodoListEvent()
}