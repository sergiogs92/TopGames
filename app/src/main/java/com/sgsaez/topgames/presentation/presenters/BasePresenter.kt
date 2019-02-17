package com.sgsaez.topgames.presentation.presenters

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T> {

    var compositeDisposable = CompositeDisposable()

    var view: T? = null
        private set

    fun attachView(view: T) {
        this.view = view
    }

    fun disposeComposite(){
        compositeDisposable.dispose()
    }

    fun detachView() {
        this.view = null
    }

    val isViewAttached: Boolean
        get() = view != null

}
