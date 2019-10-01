@file:Suppress("unused")

package com.github.underlow.kredux

import react.*
import kotlin.reflect.KClass

typealias MapStateToProps<State, Props> = Props.(State) -> Unit

fun <State, P : RProps, S : RState> Store<State>.connect(
    wrappedComponent: KClass<out RComponent<P, S>>,
    dispatcher: MapStateToProps<State, P> = {}
): RBuilder.(RElementBuilder<P>.() -> Unit) -> Unit {
    return { block ->
        val connectFunction = connectToStore(wrappedComponent, dispatcher)
        val connectedRClass = connectFunction(this@connect, block)
        connectedRClass.invoke {}
    }
}

private fun <State, P : RProps, S : RState> Store<State>.connectToStore(
    wrappedComponent: KClass<out RComponent<P, S>>,
    dispatcher: MapStateToProps<State, P>
): (Store<State>, RElementBuilder<P>.() -> Unit) -> RClass<P> =
    { store, block ->
        rFunction("Wrapper for ${wrappedComponent.simpleName}") {
            child(Wrapper::class) {
                val handler: RBuilder.(dynamic) -> Unit = {
                    child(wrappedComponent) {
                        // props of inner (wrapped) component
                        // initial props dispatch
                        this.attrs.dispatcher(it as State)
                        // apply props provided on call site
                        // this line allows pass props manually
                        block.invoke(this)
                    }
                }
                // props of outer (wrapper) component
                attrs.wrappedComponent = handler
                attrs.store = store
            }
        }
    }

class Wrapper : RComponent<Wrapper.Props, RState>() {
    private var unsubscribe: () -> Unit = {}
    private var globalState: dynamic = null

    override fun componentWillMount() {
        this.unsubscribe = props.store.subscribe { state ->
            globalState = state
            this.forceUpdate()
        }
    }

    override fun componentWillUnmount() {
        unsubscribe()
    }

    override fun RBuilder.render() {
        child(buildElement {
            props.wrappedComponent.invoke(this, globalState)
        }!!)
    }

    interface Props : RProps {
        var wrappedComponent: RBuilder.(dynamic) -> Unit
        var store: Store<*>
    }
}
