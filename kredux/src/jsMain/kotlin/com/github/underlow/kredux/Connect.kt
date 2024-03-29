@file:Suppress("unused")

package com.github.underlow.kredux

import react.*
import kotlin.reflect.KClass

/**
 * Type of function that gets state and update component properties with values extracted from state
 *
 * Usually it looks like this:
 * fun <S,P> mapStateToProps(): MapStateToProps<S,P> = {
 *       this.active = (it.visibilityFilter == this.filter)
 *   }
 * NOTE that in this example mapStateToProps return function
 */
typealias MapStateToProps<State, Props> = Props.(State) -> Unit

/**
 * Suppose we have hierarchy Comp1->Comp2->Comp3  and all are connected to store,
 * after state update Comp1 will be updated and will trigger update of Comp2 and Comp3,
 * then Comp2 will receive update and trigger update for Comp3 and so on.
 *
 * To prevent such massive updates validator function might be provided which check if component properties has been changed.
 *
 * Usually it looks like this:
 * fun <S,P> validateChanges(): MapStateToProps<S,P> {
 *      return this.visibilityFilter != it.filter // return true if values are different
 *   }
 *
 */
typealias PropsChangeValidator<State, Props> = Props.(State) -> Boolean

/**
 * Connect RComponent to store.
 * @param component to be connected component
 * @param propsChangeValidator function to validate if component should be rebuild see [PropsChangeValidator]
 * @param dispatcher mapStateToProps function see [MapStateToProps]
 *
 *  example:
 *
 *  fun validateChanges(): PropsChangeValidator<StoreState, UserPage.Props> = {
 *      userData != it.userDataState.data ||
 *           loading != it.userDataState.loading
 *   }
 *
 *  val userPage by lazy {
 *       store.connect(UserPage::class, validateChanges()){
 *           userData = it.userDataState.data
 *           loading = it.userDataState.loading
 *       }
 *   }
 *
 */
fun <StoreState, P : Props, S : State> Store<StoreState>.connect(
    component: KClass<out RComponent<P, S>>,
    propsChangeValidator: PropsChangeValidator<StoreState, P> = { true },
    dispatcher: MapStateToProps<StoreState, P> = {}
): RBuilder.(RElementBuilder<P>.() -> Unit) -> Unit {
    return { block ->
        val connectFunction = connectToStore(component, propsChangeValidator, dispatcher)
        val connectedRClass = connectFunction(this@connect, block)
        connectedRClass.invoke {}
    }
}

/**
 * this wrapper is required to pass [PropsChangeValidator] to outer scope.
 * Since we know nothing about connected component we do not have even props type we have to use lambda to pass required values to wrapper.
 */
class ValidatorWrapper<State>(var propsChangeValidator: ((State) -> Boolean)?) {
    fun validate(state: State): Boolean = propsChangeValidator?.invoke(state) ?: false

}

private fun <StoreState, P : Props, S : State> Store<StoreState>.connectToStore(
    wrappedComponent: KClass<out RComponent<P, S>>,
    propsChangeValidator: PropsChangeValidator<StoreState, P>,
    dispatcher: MapStateToProps<StoreState, P>
): (Store<StoreState>, RElementBuilder<P>.() -> Unit) -> FC<P> =
    { store, block ->
        functionComponent("Wrapper for ${wrappedComponent.simpleName}") {
            child(Wrapper::class) {
                val validatorWrapper = ValidatorWrapper<StoreState>(null)

                val handler: RBuilder.(dynamic) -> Unit = {
                    child(wrappedComponent) {
                        // pass validator with this@child.attrs to outer scope
                        if (validatorWrapper.propsChangeValidator == null)
                            validatorWrapper.propsChangeValidator = { state ->
                                this.attrs.propsChangeValidator(state)
                            }
                        // props of inner (wrapped) component
                        // apply props provided on call site
                        // this line allows pass props manually
                        block.invoke(this)
                        // props dispatch
                        this.attrs.dispatcher(it as StoreState)
                    }
                }

                // props of outer (wrapper) component
                attrs.wrappedComponent = handler
                attrs.store = store
                attrs.validatorWrapper = validatorWrapper
            }
        }
    }


class Wrapper : RComponent<Wrapper.WrapperProps, State>() {
    private var unsubscribe: () -> Unit = {}
    private var globalState: dynamic = null

    override fun componentWillMount() {
        this.unsubscribe = props.store.subscribe { state ->
            globalState = state
            if (props.validatorWrapper.validate(globalState))
                this.forceUpdate()
        }
    }

    override fun componentWillUnmount() {
        unsubscribe()
    }

    override fun RBuilder.render() {
        child(buildElement {
            props.wrappedComponent.invoke(this, globalState)
        })
    }

    interface WrapperProps : Props {
        var wrappedComponent: RBuilder.(dynamic) -> Unit
        var store: Store<*>
        var validatorWrapper: ValidatorWrapper<*>
    }
}
