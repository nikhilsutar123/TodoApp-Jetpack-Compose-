package com.example.todoapp.view.add_edit_todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.util.UiEvent
import com.example.todoapp.view.todolistscreen.TodoListEvent
import kotlin.math.max

@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }

                else -> Unit
            }

        }
    }
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), floatingActionButton = {
        FloatingActionButton(onClick = { viewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick) }) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
        }
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            println("in addEditTodoScreen -> ${viewModel.title}, ${viewModel.desc}")
            TextField(
                value = viewModel.title,
                onValueChange = { viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it)) },
                placeholder = {
                    Text(text = "title")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            TextField(
                value = viewModel.desc,
                onValueChange = { viewModel.onEvent(AddEditTodoEvent.OnDescChange(it)) },
                placeholder = {
                    Text(text = "description")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5
            )
        }
    }
}