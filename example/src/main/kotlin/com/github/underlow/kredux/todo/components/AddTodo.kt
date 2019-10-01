package com.github.underlow.kredux.todo.components

import com.github.underlow.kredux.connect
import com.github.underlow.kredux.todo.redux.AddTodoAction
import com.github.underlow.kredux.todo.store
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.form
import react.dom.input


class AddTodo(props: RProps) : RComponent<RProps, RState>(props) {
    private val inputRef = createRef<HTMLInputElement>()
    override fun RBuilder.render() {
        div {
            form {
                attrs.onSubmitFunction = { event ->
                    event.preventDefault()
                    inputRef.current!!.let {
                        if (it.value.trim().isNotEmpty()) {
                            store.dispatch(AddTodoAction(it.value))
                            it.value = ""
                        }
                    }
                }
                input(type = InputType.text) {
                    ref = inputRef
                }
                button(type = ButtonType.submit) {
                    +"Add Todo"
                }
            }
        }
    }
}


val addTodo by kotlin.lazy { store.connect(AddTodo::class) }