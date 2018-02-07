package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.presentation.model.GameViewModel

interface FavouriteListView {
    fun addFavouriteToList(favouriteGames: List<GameViewModel>)
    fun showNoDataFoundError()
    fun removeFavouriteToList(favouriteGame: GameViewModel)
    fun navigateToGame(favouriteGame: GameViewModel)
}