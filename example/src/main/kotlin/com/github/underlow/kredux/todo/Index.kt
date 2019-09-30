package com.github.underlow.kredux.todo

import com.github.underlow.kredux.ExtensionReducer
import com.github.underlow.kredux.RAction
import com.github.underlow.kredux.createStore
import com.github.underlow.kredux.todo.components.addTodo
import com.github.underlow.kredux.todo.components.footer
import com.github.underlow.kredux.todo.components.todoList
import com.github.underlow.kredux.todo.redux.*
import react.dom.br
import react.dom.div
import react.dom.render
import kotlin.browser.document


val combineReducers: ExtensionReducer<State> = { state, action ->
    state.copy(
        todos = todos(state, action),
        visibilityFilter = visibilityFilter(state, action)
    )
}


private fun todos(state: State, action: RAction): Array<Todo> = when (action) {
    is AddTodoAction -> state.todos + Todo(action.id, action.text, false)
    is ToggleTodo -> state.todos.map {
        if (it.id == action.id) {
            it.copy(completed = !it.completed)
        } else {
            it
        }
    }.toTypedArray()
    else -> state.todos
}

private fun visibilityFilter(state: State, action: RAction): VisibilityFilter =
    when (action) {
        is SetVisibilityFilter -> action.filter
        else -> state.visibilityFilter
    }

val store = createStore(combineReducers, State())


fun main() {
    val rootDiv = document.getElementById("root")
    render(rootDiv) {
        div {
            addTodo {}
            todoList {}
            footer()
            br {}
        }
    }
}