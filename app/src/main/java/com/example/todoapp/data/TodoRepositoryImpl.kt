package com.example.todoapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class TodoRepositoryImpl(private val dao: TodoDao) : TodoRepository {
    override suspend fun insertTodo(todo: Todo) = withContext(Dispatchers.IO) {
        dao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) = withContext(Dispatchers.IO) {
        dao.deleteTodo(todo)
    }

    override suspend fun getTodoById(id: Int): Todo? = withContext(Dispatchers.IO) {
        return@withContext dao.getTodoById(id)
    }

    override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos().flowOn(Dispatchers.IO)
    }
}