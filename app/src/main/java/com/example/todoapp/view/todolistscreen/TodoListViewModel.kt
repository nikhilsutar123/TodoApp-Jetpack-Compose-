package com.example.todoapp.view.todolistscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.util.Constants
import com.example.todoapp.util.Routes
import com.example.todoapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository,
) : ViewModel() {
    val todos = repository.getTodos()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    private val _deletedTodo = MutableStateFlow<Todo?>(null)
    val deletedTodo: StateFlow<Todo?> = _deletedTodo


    fun onEvent(event: TodoListEvent) {
        val savedStateHandle: SavedStateHandle? = null
        when (event) {
            is TodoListEvent.OnTodoClick -> {
                savedStateHandle?.set(Constants.PURPOSE_ARG, "edit")
                savedStateHandle?.set(Constants.TODO_ID_ARG, event.todo.id)
                val route  = Routes.ADD_EDIT_TODO +
                        "?${Constants.TODO_ID_ARG}=${event.todo.id}&${Constants.PURPOSE_ARG}=edit"
                println("Generated route: $route")
                sendUiEvent(
                    UiEvent.Navigate(
                        Routes.ADD_EDIT_TODO +
                                "?${Constants.TODO_ID_ARG}=${event.todo.id}&${Constants.PURPOSE_ARG}=edit"
                    )
                )
            }

            is TodoListEvent.OnAddTodoClick -> {
                savedStateHandle?.set(Constants.PURPOSE_ARG, "add")
                viewModelScope.launch {
                    sendUiEvent(UiEvent.Navigate("${Routes.ADD_EDIT_TODO}?${Constants.PURPOSE_ARG}=add"))
                }

            }

            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    repository.deleteTodo(event.todo)
                    _deletedTodo.value = event.todo

                    sendUiEvent(
                        UiEvent.ShowSnackBar(
                            message = Constants.TODO_DELETE_MSG,
                            action = Constants.TODO_DELETE_ACTION,
                        )
                    )
                }
            }

            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }

            TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo.let { todo ->
                    viewModelScope.launch { repository.insertTodo(todo.value!!) }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}