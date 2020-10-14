package com.sgsaez.topgames.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgsaez.topgames.support.domains.functional.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<S>(baseState: Class<S>) : ViewModel() {

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
        viewModelScope.launch {
            withContext(Dispatchers.IO) { action() }.let(onCompleted)
        }
    }

}
