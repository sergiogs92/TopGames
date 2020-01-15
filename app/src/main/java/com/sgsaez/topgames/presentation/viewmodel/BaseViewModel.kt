package com.sgsaez.topgames.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgsaez.topgames.support.domains.functional.Either
import kotlinx.coroutines.*

abstract class BaseViewModel<S>(baseState: Class<S>) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _state = MutableLiveData<S>()
    val state: LiveData<S> get() = _state

    protected val screenState get() = _state.value!!

    init {
        _state.value = baseState.newInstance()
    }

    protected fun updateState(state: S) {
        _state.value = state
    }

    protected fun <L, R> launchTask(action: () -> Either<L, R>, onCompleted: (Either<L, R>) -> Unit) {
        uiScope.launch {
            withContext(Dispatchers.IO) { action() }.let(onCompleted)
        }
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

}
