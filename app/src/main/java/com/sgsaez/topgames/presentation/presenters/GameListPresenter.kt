package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.GetGames
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.utils.SchedulerProvider

class GameListPresenter(private val getGames: GetGames,
                        private val schedulerProvider: SchedulerProvider) : BasePresenter<GameListView>() {

    fun onLoadGames(query: String, isRefresh: Boolean = false) {
        getGames.execute(query)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .subscribe({ games ->
                    if (isRefresh) view?.clearList()
                    view?.addGameToList(games)
                    view?.hideLoading()
                }, {
                    view?.hideLoading()
                    view?.showToastError(it.message)
                })
    }

    fun onGameClicked(game: Game) {
        view?.navigateToGame(game)
    }

    fun onSearchClicked(query: String) {
        view?.navigateToGameList(query)
    }

}