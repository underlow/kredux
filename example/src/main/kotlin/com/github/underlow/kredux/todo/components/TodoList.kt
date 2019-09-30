package com.github.underlow.kredux.todo.components

import com.github.underlow.kredux.connect
import com.github.underlow.kredux.todo.redux.Todo
import com.github.underlow.kredux.todo.redux.ToggleTodo
import com.github.underlow.kredux.todo.redux.VisibilityFilter
import com.github.underlow.kredux.todo.store
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.ul

interface TodoListProps : RProps {
    var todos: Array<Todo>
}

class TodoList(props: TodoListProps) : RComponent<TodoListProps, RState>(props) {
    override fun RBuilder.render() {
        ul {
            props.todos.forEach { todo(it) { store.dispatch(ToggleTodo(it.id)) } }
        }
    }
}

val todoList = store.connect(TodoList::class) { state ->
    this.todos = getVisibleTodos(state.todos, state.visibilityFilter)
}

private fun getVisibleTodos(todos: Array<Todo>, filter: VisibilityFilter): Array<Todo> = when (filter) {
    VisibilityFilter.SHOW_ALL -> todos
    VisibilityFilter.SHOW_ACTIVE -> todos.filter { !it.completed }.toTypedArray()
    VisibilityFilter.SHOW_COMPLETED -> todos.filter { it.completed }.toTypedArray()
}
