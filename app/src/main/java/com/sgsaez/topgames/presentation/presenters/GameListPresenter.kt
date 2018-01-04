package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.GetGames
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.utils.SchedulerProvider

class GameListPresenter(private val getGames: GetGames,
                        private val schedulerProvider: SchedulerProvider) : BasePresenter<GameListView>() {

    fun getGames(isRefresh: Boolean = false) {
        getGames.execute()
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .subscribe({ games ->
                    if (isRefresh) view?.clearList()
                    view?.addGameToList(games)
                    view?.hideLoading()
                }, {
                    view?.hideLoading()
                    view?.showEmptyListError()
                    view?.showToastError()
                })
    }
}