package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.domain.game.GamesException.Companion.ERROR_INTERNET_CONNECTION
import com.sgsaez.topgames.domain.game.GamesException.Companion.ERROR_NO_DATA_FOUND
import com.sgsaez.topgames.domain.game.GetGames
import com.sgsaez.topgames.presentation.model.GameViewModel
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
                    val gamesException = it as GamesException
                    when (gamesException.tag) {
                        ERROR_NO_DATA_FOUND -> view?.showNoDataFoundError()
                        ERROR_INTERNET_CONNECTION -> view?.showInternetConnectionError()
                        else -> view?.showDefaultError()
                    }
                })
    }

    fun onGameClicked(game: GameViewModel) {
        view?.navigateToGame(game)
    }

    fun onSearchClicked(query: String) {
        view?.navigateToGameList(query)
    }

}