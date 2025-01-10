package com.example.todoapp.view.todolistscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.util.Constants
import com.example.todoapp.util.UiEvent
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val todos by viewModel.todos.collectAsState(initial = emptyList())
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val (selectedTab, setSelectedTab) = remember { mutableIntStateOf(0) }

    val colorSaver = Saver<Color, FloatArray>(save = { color ->
        floatArrayOf(
            color.red,
            color.green,
            color.blue,
            color.alpha
        )
    }, restore = { floats -> Color(floats[0], floats[1], floats[2], floats[3]) })
    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    launch {
                        val result = snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action,
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                        }
                    }
                }

                is UiEvent.Navigate -> {
                    onNavigate(event)
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TodoListEvent.OnAddTodoClick)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (todos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No Todos available. Add some Tasks!",
                        softWrap = true,
                        textAlign = TextAlign.Center
                    )
                }
            } else
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Todos",
                        textAlign = TextAlign.Start,
                        fontSize = TextUnit(value = 24f, type = TextUnitType.Sp),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    TabRow(selectedTabIndex = selectedTab) {
                        Tab(selected = selectedTab == 0, onClick = { setSelectedTab(0) }, text = {
                            Text(
                                text = Constants.TODO_ONGOING,
                            )
                        })
                        Tab(selected = selectedTab == 1, onClick = { setSelectedTab(1) }, text = {
                            Text(
                                text = Constants.TODO_COMPLETED
                            )
                        })
                    }
                    val filteredTodos = when (selectedTab) {
                        0 -> todos.filter { task -> !task.isDone }
                        1 -> todos.filter { task -> task.isDone }
                        else -> todos
                    }
                    if (filteredTodos.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.padding(it)) {
                            items(filteredTodos) { todo ->
                                val bgColor = rememberSaveable(saver = colorSaver) {
                                    Color(
                                        red = Random.nextFloat() * 0.5f + 0.5f,
                                        green = Random.nextFloat() * 0.5f + 0.5f,
                                        blue = Random.nextFloat() * 0.5f + 0.5f,
                                        alpha = 1f
                                    )
                                }
                                TodoListItem(
                                    todo = todo,
                                    onEvent = viewModel::onEvent,
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                                        }
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    backgroundColor = bgColor
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (selectedTab == 0) Constants.TODO_ONGOING_MSG else Constants.TODO_COMPLETED_MSG,
                                softWrap = true,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
        }


    }

}