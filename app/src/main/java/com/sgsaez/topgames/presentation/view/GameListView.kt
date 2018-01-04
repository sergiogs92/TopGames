package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.presentation.model.Game

interface GameListView {
    fun showLoading()
    fun hideLoading()
    fun addGameToList(games: List<Game>)
    fun showEmptyListError()
    fun hideEmptyListError()
    fun showToastError(message: String?)
    fun clearList()
}