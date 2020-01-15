package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.presentation.model.Game

interface FavouriteListView {
    fun addFavouriteToList(favouriteGames: List<Game>)
    fun showNoDataFoundError()
    fun removeFavouriteToList(favouriteGame: Game)
    fun navigateToGame(favouriteGame: Game)
}