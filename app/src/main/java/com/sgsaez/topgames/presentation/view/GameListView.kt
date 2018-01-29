package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.domain.ErrorConstants
import com.sgsaez.topgames.presentation.model.Game

interface GameListView {
    fun showLoading()
    fun hideLoading()
    fun addGameToList(games: List<Game>)
    fun showToastError(tag: ErrorConstants)
    fun clearList()
    fun navigateToGame(game: Game)
    fun navigateToGameList(query: String)
}