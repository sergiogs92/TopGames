package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.domain.ErrorConstants
import com.sgsaez.topgames.presentation.model.GameViewModel

interface GameListView {
    fun showLoading()
    fun hideLoading()
    fun addGameToList(games: List<GameViewModel>)
    fun showToastError(tag: ErrorConstants)
    fun clearList()
    fun navigateToGame(game: GameViewModel)
    fun navigateToGameList(query: String)
}