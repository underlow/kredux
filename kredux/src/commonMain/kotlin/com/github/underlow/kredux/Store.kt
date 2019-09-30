package com.github.underlow.kredux

/**
 * all calls to dispatch must be thread confined to prevent races
 */
class Store<State>(private val reducer: ExtensionReducer<State>, state: State) {
    var state = state
        private set

    private val subscribers = ArrayList<(State) -> Unit>()

    /**
     * @throws [IllegalStateException] if dispatch already in progress
     */
    fun dispatch(action: RAction) = guard {
        state = reducer.invoke(this, this.state, action)
        subscribers.forEach { it.invoke(state) }
    }

    fun subscribe(listener: (State) -> Unit): () -> Unit {
        subscribers.add(listener)
        listener.invoke(state)

        return { subscribers.remove(listener) }
    }

    private var dispatchInProgress = false

    private fun guard(block: () -> Unit) {
        check(!dispatchInProgress) { "Dispatch already in progress" }

        dispatchInProgress = true
        try {
            block()
        } finally {
            dispatchInProgress = false
        }
    }
}


fun <State> createStore(reducer: PureReducer<State>, state: State): Store<State> = Store(reducer.asExtension(), state)

fun <State> createStore(reducer: ExtensionReducer<State>, state: State): Store<State> = Store(reducer, state)

/**
 * Just to convert type [PureReducer] to [ExtensionReducer]
 */
private fun <State> PureReducer<State>.asExtension(): ExtensionReducer<State> = { s, a -> this@asExtension(s, a) }
