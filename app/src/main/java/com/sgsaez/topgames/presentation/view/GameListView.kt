package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.support.domains.Page

interface GameListView {
    fun showLoading()
    fun hideLoading()
    fun addGameToList(page: Page<GameViewModel>)
    fun showNoDataFoundError()
    fun showInternetConnectionError()
    fun showDefaultError()
    fun clearList()
    fun navigateToGame(game: GameViewModel)
    fun navigateToGameList(query: String)
    fun navigateToFavourites()
}