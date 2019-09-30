package com.github.underlow.kredux

/**
 * All dispatched actions should implement this
 */
interface RAction

/**
 * [Store.dispatch] can be called from [ExtensionReducer] making it useful for async.
 * NOTE: call [Store.dispatch] only asynchronously, i.e. from coroutine
 * ```
 *  fun Store<State>.reducer(state: Stare, action: RAction): PartialState {
 *    return state.copy(isLoading = true)
 *    launch {
 *        // do some work
 *        dispatch(DataLoaded)
 *      }
 *  }
 *  ```
 */
typealias ExtensionReducer<State> = Store<State>.(State, RAction) -> State

typealias PureReducer<State> = (State, RAction) -> State

