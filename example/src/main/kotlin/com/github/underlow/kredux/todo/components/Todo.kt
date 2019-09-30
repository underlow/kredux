package com.github.underlow.kredux.todo.components

import com.github.underlow.kredux.todo.redux.Todo
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.textDecoration
import kotlinx.html.js.onClickFunction
import react.RBuilder
import styled.css
import styled.styledLi

fun RBuilder.todo(todo: Todo, onClick: () -> Unit) =
    styledLi {
        attrs.onClickFunction = { onClick() }
        css {
            if (todo.completed) textDecoration(TextDecorationLine.lineThrough)
        }
        +todo.text
    }