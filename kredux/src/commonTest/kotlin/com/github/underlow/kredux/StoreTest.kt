package com.github.underlow.kredux

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class StoreTest {
    @Suppress("UNUSED_PARAMETER")
    @Test
    fun unsubscribe() {
        val state = State(0, 0.0)

        fun singleReducer(s: State, action: RAction): State = s

        val store = createStore(::singleReducer, state)

        var calls = 0
        store.subscribe { } // fill subscriptions list
        val unsubscribe = store.subscribe {
            calls++
            assertTrue("Should not be called after unsubscribe()") {
                // once in subscribe, once in dispatch
                calls < 3
            }
        }
        store.dispatch(Inc)
        unsubscribe()
        store.dispatch(Inc)
    }

    @Suppress("UNUSED_PARAMETER")
    @Test
    fun concurrentDispatch() {
        val state = State(0, 0.0)

        fun singleReducer(s: State, action: RAction): State = s

        val store = createStore(::singleReducer, state)
        var calls = 0

        store.subscribe {
            // to skip first listener call while subscribing
            if (calls > 0)
                assertFailsWith<IllegalStateException> {
                    store.dispatch(Inc)
                }
            calls++
        }
        store.dispatch(Inc)
    }
}