package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.domain.game.GetGames
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.support.domains.Page
import com.sgsaez.topgames.support.domains.functional.fold

class GameListPresenter(private val getGames: GetGames) : BasePresenter<GameListView>() {

    fun onLoadGames(page: Page<GameViewModel>, isRefresh: Boolean = false) {
        launchTask(action = { getGames.execute(page.requestedPage.toString(), page.query) },
                onCompleted = {
                    it.fold({ error -> error.toGetGamesThrowable() },
                            { games -> onCompleteGetGames(page.query.isNotEmpty(), isRefresh, games) })
                })
    }


    private fun onCompleteGetGames(isQuery: Boolean, isRefresh: Boolean, games: List<GameViewModel>) {
        view?.hideLoading()
        if (isRefresh) view?.clearList()
        view?.addGameToList(isQuery = isQuery, games = games)
    }

    private fun GamesException.toGetGamesThrowable(): Unit? {
        view?.hideLoading()
        return when (error) {
            GameError.ERROR_NO_DATA_FOUND -> view?.showNoDataFoundError()
            GameError.ERROR_INTERNET_CONNECTION -> view?.showInternetConnectionError()
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