package com.github.underlow.kredux.todo.components

import com.github.underlow.kredux.connect
import com.github.underlow.kredux.todo.redux.VisibilityFilter
import com.github.underlow.kredux.todo.store
import kotlinx.css.px
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledButton

interface LinkProps : RProps {
    var active: Boolean
    var onClick: () -> Unit
    var filter: VisibilityFilter
}

class Link(props: LinkProps) : RComponent<LinkProps, RState>(props) {
    override fun RBuilder.render() {
        styledButton {
            attrs.onClickFunction = { props.onClick() }
            attrs.disabled = props.active
            css {
                marginLeft = 4.px
            }
            children()
        }
    }
}

val link by lazy {
    store.connect(Link::class) {
        this.active = (it.visibilityFilter == this.filter)
    }
}