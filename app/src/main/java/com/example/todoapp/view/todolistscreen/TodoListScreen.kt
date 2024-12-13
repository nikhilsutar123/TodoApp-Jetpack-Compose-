package com.example.todoapp.view.todolistscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.util.UiEvent

@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val todos by viewModel.todos.collectAsState(initial = emptyList())
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    LaunchedEffect(key1 = true) {
        println("LaunchedEffect triggered")
        viewModel.uiEvent.collect { event ->
            println("UiEvent collected in LaunchedEffect: $event")
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    println("Showing Snackbar: ${event.message}")
                    val result = snackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }

                is UiEvent.Navigate -> {
                    println("Navigation event: ${event.route}")
                    (onNavigate(event))
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                println("fab for add todo clicked...")
                viewModel.onEvent(TodoListEvent.OnAddTodoClick)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        if (todos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No Todos available. Add some Tasks!",
                    softWrap = true,
                    textAlign = TextAlign.Center
                )
            }
        } else
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                println("LazyColumn recomposed with todos: $todos")
                items(todos) { todo ->
                    TodoListItem(
                        todo = todo,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier
                            .clickable {
                                println("data ${todo.id}, ${todo.title}")
                                viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                            }
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }

    }

}