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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.todoapp.util.Constants
import com.example.todoapp.util.UiEvent
import com.example.todoapp.view.todolistscreen.TodoListEvent
import kotlin.math.max

@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel(),
    savedStateHandle: SavedStateHandle? = null
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val purpose = savedStateHandle?.get<String>(Constants.PURPOSE_ARG) ?: "add"
    val title  = if(purpose == "edit") "Edit Todo" else "Add Todo"
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                }
                else -> Unit
            }

        }
    }
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick) }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
            }
        }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                fontSize = TextUnit(value = 24f, type = TextUnitType.Sp),
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(Modifier.height(16.dp))
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