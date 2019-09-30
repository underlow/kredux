package com.github.underlow.kredux.todo.redux

data class Todo(val id: Int, val text: String, var completed: Boolean)


enum class VisibilityFilter {
    SHOW_ALL,
    SHOW_ACTIVE,
    SHOW_COMPLETED
}

@Suppress("ArrayInDataClass")
data class State(
    val todos: Array<Todo> = emptyArray(),
    val visibilityFilter: VisibilityFilter = VisibilityFilter.SHOW_ALL
)