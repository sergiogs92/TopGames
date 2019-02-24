package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.domain.game.GetGames
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.support.SchedulerProvider
import com.sgsaez.topgames.support.domains.Page

class GameListPresenter(private val getGames: GetGames, private val schedulerProvider: SchedulerProvider) : BasePresenter<GameListView>() {

    fun onLoadGames(page: Page<GameViewModel>, isRefresh: Boolean = false) {
        addDisposable(getGames.execute(page.requestedPage.toString(), page.query)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .subscribe({ onCompleteGetGames(page.query.isNotEmpty(), isRefresh, it) }, { it.toGetGamesThrowable() }))
    }

    private fun onCompleteGetGames(isQuery: Boolean, isRefresh: Boolean, games: List<GameViewModel>) {
        view?.hideLoading()
        if (isRefresh) view?.clearList()
        view?.addGameToList(isQuery = isQuery, games = games)
    }

    private fun Throwable.toGetGamesThrowable(): Unit? {
        view?.hideLoading()
        return when {
            this is GamesException -> when (error) {
                GameError.ERROR_NO_DATA_FOUND -> view?.showNoDataFoundError()
                GameError.ERROR_INTERNET_CONNECTION -> view?.showInternetConnectionError()
                else -> view?.showDefaultError()
            }
            else -> view?.showDefaultError()
        }
    }

    fun onGameClicked(game: GameViewModel) {
        view?.navigateToGame(game)
    }

    fun onSearchClicked(query: String) {
        view?.navigateToGameList(query)
    }

    fun onFavouritesClicked() {
        view?.navigateToFavourites()
    }

    fun onLoadMore(requestPage: Int) {
        onLoadGames(Page(requestedPage = requestPage))
    }

}