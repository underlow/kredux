package com.github.underlow.kredux

import kotlin.test.Test
import kotlin.test.assertEquals

class ReducerTest {

    @Test
    fun reducerTest() {
        val combineReducers: ExtensionReducer<State> = { state, action ->
            state.copy(
                i = reduce(state, action),
                d = state.d
            )
        }
        val store = createStore(combineReducers, State(0, 0.0))

        assertEquals(State(1, 0.0), combineReducers.invoke(store, store.state, Inc))
    }

    @Test
    fun singleReducerTest() {
        val state = State(0, 0.0)

        val store = createStore(::singleReducer, state)

        store.subscribe {}
        store.dispatch(Inc)
        store.dispatch(IncDouble)

        assertEquals(State(1, 1.0), store.state)
    }

    @Test
    fun singleExtensionReducerTest() {
        val state = State(0, 0.0)

        val store = createStore(Store<State>::singleExtensionReducer, state)

        store.subscribe {}
        store.dispatch(Inc)
        store.dispatch(IncDouble)

        assertEquals(State(1, 1.0), store.state)
    }


    @Test
    fun basicDispatchTestCombinedPureReducers() {

        val combineReducers: ExtensionReducer<State> = { state, action ->
            state.copy(
                i = this.reducer(state, action),
                d = reduceDouble(state, action)
            )
        }
        val store = createStore(combineReducers, State(0, 0.0))

        store.subscribe {}
        store.dispatch(Inc)
        store.dispatch(IncDouble)

        assertEquals(State(1, 1.0), store.state)
    }
}

private fun reduce(s: State, action: RAction): Int {
    return when (action) {
        is Inc -> s.i + 1
        else -> s.i
    }
}

private fun singleReducer(s: State, action: RAction): State {
    return when (action) {
        is Inc -> s.copy(i = s.i + 1)
        is IncDouble -> s.copy(d = s.d + 1)
        else -> s
    }
}

private fun reduceDouble(s: State, action: RAction): Double {
    return when (action) {
        is IncDouble -> s.d + 1.0
        else -> s.d
    }
}

@Suppress("unused")
fun Store<State>.reducer(state: State, action: RAction): Int = reduce(state, action)

@Suppress("unused")
fun Store<State>.singleExtensionReducer(state: State, action: RAction): State = singleReducer(state, action)

data class State(val i: Int, val d: Double)
object Inc : RAction
object IncDouble : RAction
