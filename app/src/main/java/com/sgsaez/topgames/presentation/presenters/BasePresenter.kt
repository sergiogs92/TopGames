package com.sgsaez.topgames.presentation.presenters

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T> {

    private val compositeDisposable = CompositeDisposable()

    var view: T? = null
        private set

    fun attachView(view: T) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun disposeComposite(){
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

}
