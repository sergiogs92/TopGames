package com.sgsaez.topgames.presentation.viewmodel

import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.domain.game.GetGames
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.support.domains.Page
import com.sgsaez.topgames.support.domains.functional.fold
import com.sgsaez.topgames.support.domains.update

class GameListViewModel(val getGames: GetGames) : BaseViewModel<GameListState>(GameListState::class.java) {

    fun showGames(isRefresh: Boolean = false) {
        if (isRefresh) updateState(screenState.copy(page = Page()))
        if (screenState.page.items.isEmpty()) onLoadGames(page = screenState.page)
        else updateState(screenState.copy(page = screenState.page))
    }

    fun onLoadMore(requestPage: Int) {
        updateState(screenState.copy(page = screenState.page.update(Page(requestedPage = requestPage))))
        onLoadGames(page = screenState.page)
    }

    private fun onLoadGames(page: Page<Game>) {
        updateState(screenState.copy(isLoading = screenState.page.items.isEmpty()))
        launchTask(action = { getGames.execute(page.requestedPage.toString(), page.query) },
                onCompleted = {
                    it.fold({ error -> error.toGetGameError() },
                            { games -> onCompleteGetGames(games) })
                })
    }

    private fun GameError.toGetGameError(): Unit? {
        updateState(screenState.copy(isLoading = false))
        return when (this) {
            GameError.GamesNotFound -> updateState(screenState.copy(error = GameListError.NO_DATA_FOUND))
            GameError.NetworkConnection -> updateState(screenState.copy(error = GameListError.INTERNET_CONNECTION_ERROR))
            else -> updateState(screenState.copy(error = GameListError.DEFAULT_ERROR))
        }
    }

    private fun onCompleteGetGames(games: List<Game>) {
        updateState(screenState.copy(isLoading = false, page = screenState.page.update(Page(items = games))))
    }

}

data class GameListState(
        val isLoading: Boolean = false,
        val page: Page<Game> = Page(),
        val error: GameListError = GameListError.NONE
)

enum class GameListError {
    INTERNET_CONNECTION_ERROR,
    NO_DATA_FOUND,
    DEFAULT_ERROR,
    NONE
}