package com.sgsaez.topgames.presentation.presenters

abstract class BasePresenter<T> {

    var view: T? = null
        private set

    fun attachView(view: T) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

}
