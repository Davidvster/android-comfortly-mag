package com.dv.comfortly.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class BaseViewModel<State : Any, Event : Any> constructor(idleState: State) : ViewModel() {

    private val states: MutableStateFlow<ViewState<State>> = MutableStateFlow(ViewState(idleState))

    fun viewStates(): Flow<ViewState<State>> = states

    private val events: MutableSharedFlow<Event> = MutableSharedFlow(replay = 0)

    fun viewEvents(): SharedFlow<Event> = events

    protected var viewState: State = idleState
        get() = states.value.state ?: field
        set(value) {
            val viewState = states.value
            states.value = viewState.copy(state = value)
        }

    protected var commonState: CommonState = Idle
        get() = states.value.commonState ?: field
        set(value) {
            val viewState = states.value
            states.value =
                viewState.copy(commonState = value)
        }

    protected fun emitEvent(event: Event) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    protected fun clearCommonState() {
        commonState = Idle
    }

    protected fun showLoading() {
        commonState = Loading
    }

    protected fun hideLoading() {
        commonState = Idle
    }

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
        handleError(throwable)
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(errorHandler) { block.invoke(this) }

    fun launchWithBlockingLoading(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(errorHandler) {
            try {
                showLoading()
                block.invoke(this)
            } finally {
                hideLoading()
            }
        }

    open fun handleError(throwable: Throwable) {
        when (throwable) {
            else -> commonState = Error(throwable = throwable)
        }
    }

    suspend fun <T> io(block: suspend CoroutineScope.() -> T) =
        withContext(Dispatchers.IO) { block.invoke(this) }
}
