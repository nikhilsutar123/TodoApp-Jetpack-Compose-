package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgs
import androidx.navigation.navArgument
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.util.Constants
import com.example.todoapp.util.Routes
import com.example.todoapp.view.add_edit_todo.AddEditTodoScreen
import com.example.todoapp.view.todolistscreen.TodoListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.TODO_LIST) {
                    composable(Routes.TODO_LIST) {
                        TodoListScreen(onNavigate = {
                            navController.navigate(it.route)
                        })
                    }
                    composable(Routes.ADD_EDIT_TODO + "?${Constants.TODO_ID_ARG}={${Constants.TODO_ID_ARG}}",
                        arguments = listOf(
                            navArgument(
                                name = Constants.TODO_ID_ARG
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )) {
                        AddEditTodoScreen(onPopBackStack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
