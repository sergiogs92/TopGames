package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.presentation.model.GameViewModel

interface GameDetailView {
    fun addTitleToolbar(name: String)
    fun addDescription(content: String)
    fun addImage(url: String)
    fun showSocialSharedNetworks(game: GameViewModel)
    fun showSaveFavourite()
    fun showFavouriteAlreadyExists()
    fun resetStatusBarColor()
}