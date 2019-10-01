package com.github.underlow.kredux

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

/**
 * all calls to dispatch must be thread confined to prevent races
 */
@Suppress("EXPERIMENTAL_API_USAGE")
class Store<State>(private val reducer: ExtensionReducer<State>, state: State) {
    var state = state
        private set

    private val channel = Channel<RAction>(capacity = Channel.RENDEZVOUS)
    private val subscribers = ArrayList<(State) -> Unit>()

    init {
        GlobalScope.launch {
            channel.consumeEach { receive(it) }
        }
    }

    fun dispatch(action: RAction) {
        GlobalScope.launch { channel.send(action) }
    }


    private fun receive(action: RAction) {
        state = reducer.invoke(this, this.state, action)
        subscribers.forEach { it.invoke(state) }
    }

    fun subscribe(listener: (State) -> Unit): () -> Unit {
        subscribers.add(listener)
        listener.invoke(state)

        return { subscribers.remove(listener) }
    }
}


fun <State> createStore(reducer: PureReducer<State>, state: State): Store<State> = Store(reducer.asExtension(), state)

fun <State> createStore(reducer: ExtensionReducer<State>, state: State): Store<State> = Store(reducer, state)

/**
 * Just to convert type [PureReducer] to [ExtensionReducer]
 */
private fun <State> PureReducer<State>.asExtension(): ExtensionReducer<State> = { s, a -> this@asExtension(s, a) }
