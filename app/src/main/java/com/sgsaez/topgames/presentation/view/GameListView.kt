package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.presentation.model.Game

interface GameListView {
    fun showLoading()
    fun hideLoading()
    fun addGameToList(games: List<Game>)
    fun showToastError(message: String?)
    fun clearList()
    fun navigateToGame(game: Game)
}