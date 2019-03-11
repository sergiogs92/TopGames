package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.domain.game.GetGames
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.support.domains.Page
import com.sgsaez.topgames.support.domains.functional.fold
import com.sgsaez.topgames.support.domains.update

class GameListPresenter(private val getGames: GetGames) : BasePresenter<GameListView>() {

    private var page: Page<GameViewModel> = Page()

    private fun onLoadGames(page: Page<GameViewModel>) {
        launchTask(action = { getGames.execute(page.requestedPage.toString(), page.query) },
                onCompleted = {
                    it.fold({ error -> error.toGetGameError() },
                            { games -> onCompleteGetGames(games) })
                })
    }

    private fun onCompleteGetGames(games: List<GameViewModel>) {
        view?.hideLoading()
        page = page.update(Page(items = games))
        view?.addGameToList(page)
    }

    private fun GameError.toGetGameError(): Unit? {
        view?.hideLoading()
        return when (this) {
            GameError.GamesNotFound -> view?.showNoDataFoundError()
            GameError.NetworkConnection -> view?.showInternetConnectionError()
            else -> view?.showDefaultError()
        }
    }

    fun showGames(query: String = "", isRefresh: Boolean = false) {
        when {
            page.items.isEmpty() -> {
                view?.showLoading()
                page = page.update(Page(query = query))
                onLoadGames(page)
            }
            isRefresh -> {
                view?.showLoading()
                view?.clearList()
                page = Page()
                onLoadGames(page)
            }
            else -> view?.addGameToList(page)
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
        page = page.update(Page(requestedPage = requestPage))
        onLoadGames(page)
    }

}