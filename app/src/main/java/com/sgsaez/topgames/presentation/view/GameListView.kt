package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.presentation.model.GameViewModel

interface GameListView {
    fun showLoading()
    fun hideLoading()
    fun addGameToList(games: List<GameViewModel>)
    fun showNoDataFoundError()
    fun showInternetConnectionError()
    fun showDefaultError()
    fun clearList()
    fun navigateToGame(game: GameViewModel)
    fun navigateToGameList(query: String)
    fun navigateToFavourites()
}