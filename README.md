# kredux - kotlin redux implementation for kotlin react

This is POC implementation of Redux pattern for Kotlin React. 
Although there is a wrapper over JS redux implementation [kotlin-redux](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-redux) 
and usage example [kotlin-poc-frontend-react-redux](https://github.com/lawik123/kotlin-poc-frontend-react-redux) native kotlin implementation seems provide cleaner code.

## Usage

See example (this is todo example taken from [kotlin-poc-frontend-react-redux](https://github.com/lawik123/kotlin-poc-frontend-react-redux))


### Actions

Just implement empty RAction interface

### Reducers
Reducer can be just a function (Action) -> State or extention function Store.(Action) -> State. Latter allows to create async operations.
Simple reducer:
```
private fun singleReducer(s: State, action: RAction): State {
    return when (action) {
        is Inc -> s.copy(i = s.i + 1)
        else -> s
    }
} 
```

Extention reducer, return some state, launch some async task and dispatch new action in the end 
```
 fun Store<State>.reducer(state: Stare, action: RAction): PartialState {
   return state.copy(isLoading = true)
   launch {
       // do some work
       dispatch(DataLoaded)
     }
  }

```

### Combining/decomposing reducers.
Syntax sugar similar to JS `combineReducers` function couldn't be implemented in Kotlin without js reflection, so do it manually:
```
data class State(val one: ClassOne, val two: ClassTwo)
 
val combinedReducers: ExtensionReducer<State> = { state, action ->
            state.copy(
                one = this.reducerOne(state, action),
                two = reducerTwo(state, action)
            )
        }
``` 

### Store

Nice and simple:
```
val store = createStore(reducer, State())

val store = createStore(::reducerFunction, State())
```
Where `State()` is initial State value

### Map State to Props
To connect React class to store mapStateToProps function should be implemented:
```
val linkMapsStateToProps: MapStateToProps<State, Link.Props> = { state ->
    active = (it.visibilityFilter == this.filter)
}
```
Where `Link` is react class and `active` is `Link` props 

Function can be passed to connect as lambda as well: 

```
val link = store.connect(Link::class) {
    this.active = (it.visibilityFilter == this.filter)
}
``` 

### Connect
Use `store.connect` function to create connected component. Actually connect returns RBuilder extention function that can be user in render() 
```
val link by kotlin.lazy{ 
    store.connect(Link::class) {
        this.active = (it.visibilityFilter == this.filter)
    }
}
``` 

and then: 


```
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
```

#### Changes validation

Suppose we have hierarchy Comp1->Comp2->Comp3 each one is connected to store, 
after state update Comp1 will be updated and will trigger update of Comp2 and Comp3, 
then Comp2 will receive update and trigger update for Comp3 and so on.
To prevent such massive updates validator function might be provided which check if component properties has been changed.

```
fun validateChanges(): PropsChangeValidator<State, UserPage.Props> = {
     userData != it.userDataState.data ||
           loading != it.userDataState.loading
   }

  val userPage by lazy {
       store.connect(UserPage::class, validateChanges()){
           userData = it.userDataState.data
           loading = it.userDataState.loading
       }
   }

```
**if validation function is not provided all updates delivered to all components and this could cause performance issues and
Also in some cases infinite event loop possible. If Comp3 raises some event on new props it causes update of Comp1 then Comp3 and new event**

### Dispatch


Since `store` is global just use `store.dispatch(Action)` 


## Known issues
 - ~~store and reducer should be declared in the same file as main() function (kotlin js issue?)~~  due to global variables initialization order it is safer define connected components as lazy  
 - no middleware yet
 
## Publish artifact 

Cannot get working bintray + kotlin multiplatform so github registry only

create `gradle-local.properties` file:
```
gpr.user=<user>
gpr.key=<key>
```