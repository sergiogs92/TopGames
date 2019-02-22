package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.domain.game.GetGames
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.utils.SchedulerProvider

private const val INCREMENT = 20
private const val LAST_VALUE = 100

class GameListPresenter(private val getGames: GetGames, private val schedulerProvider: SchedulerProvider) : BasePresenter<GameListView>() {

    private var initValue = 0
    private var loading = false

    fun onLoadGames(query: String, isRefresh: Boolean = false) {
        loading = true
        if (isRefresh) initValue = 0
        addDisposable(getGames.execute(initValue.toString(), query)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .subscribe({ onCompleteGetGames(isRefresh, it) }, { it.toGetGamesThrowable() }))
    }

    private fun onCompleteGetGames(isRefresh: Boolean, games: List<GameViewModel>) {
        hideLoading()
        if (isRefresh) view?.clearList()
        view?.addGameToList(games)
        initValue += initValue.plus(INCREMENT)
    }

    private fun Throwable.toGetGamesThrowable(): Unit? {
        hideLoading()
        return when {
            this is GamesException -> when (error) {
                GameError.ERROR_NO_DATA_FOUND -> view?.showNoDataFoundError()
                GameError.ERROR_INTERNET_CONNECTION -> view?.showInternetConnectionError()
                else -> view?.showDefaultError()
            }
            else -> view?.showDefaultError()
        }
    }

    private fun hideLoading(){
        loading = false
        view?.hideLoading()
    }

    fun onScrollChanged(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int) {
        when {
            loading && initValue <= LAST_VALUE -> view?.showLoading()
            !loading && initValue <= LAST_VALUE -> checkLoadGame(visibleItemCount, firstVisibleItemPosition, totalItemCount)
        }
    }

    private fun checkLoadGame(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {
        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
            onLoadGames("")
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

}