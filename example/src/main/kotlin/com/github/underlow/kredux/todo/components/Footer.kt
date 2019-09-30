package com.github.underlow.kredux.todo.components

import com.github.underlow.kredux.todo.redux.SetVisibilityFilter
import com.github.underlow.kredux.todo.redux.VisibilityFilter
import com.github.underlow.kredux.todo.store
import react.RBuilder
import react.dom.div
import react.dom.span

fun RBuilder.footer() =
    div {
        span { +"Show: " }
        link {
            attrs.filter = VisibilityFilter.SHOW_ALL
            attrs.onClick = { store.dispatch(SetVisibilityFilter(attrs.filter)) }
            +"All"
        }
        link {
            attrs.filter = VisibilityFilter.SHOW_ACTIVE
            attrs.onClick = { store.dispatch(SetVisibilityFilter(attrs.filter)) }
            +"Active"
        }
        link {
            attrs.filter = VisibilityFilter.SHOW_COMPLETED
            attrs.onClick = { store.dispatch(SetVisibilityFilter(attrs.filter)) }
            +"Completed"
        }
    }