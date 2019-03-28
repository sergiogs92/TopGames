package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.support.domains.functional.Either
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<T> : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job

    var view: T? = null
        private set

    fun attachView(view: T) {
        this.view = view
    }

    fun initJob() {
        job = Job()
    }

    fun <L, R> launchTask(action: () -> Either<L, R>, onCompleted: (Either<L, R>) -> Unit) {
        launch {
            withContext(Dispatchers.IO) { action() }.let(onCompleted)
        }
    }

    fun detachView() {
        this.view = null
    }

    fun cancelJob() {
        job.cancel()
    }

}
