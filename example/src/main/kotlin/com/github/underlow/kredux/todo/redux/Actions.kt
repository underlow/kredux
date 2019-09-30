package com.github.underlow.kredux.todo.redux

import com.github.underlow.kredux.RAction


class SetVisibilityFilter(val filter: VisibilityFilter) : RAction

class AddTodoAction(val text: String) : RAction {
    private companion object {
        var nextTodoId = 1
    }

    val id = nextTodoId++
}

class ToggleTodo(val id: Int) : RAction


