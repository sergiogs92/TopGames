package com.sgsaez.topgames.presentation.view

import com.sgsaez.topgames.presentation.model.Game

interface GameDetailView {
    fun addTitleToolbar(name: String)
    fun addDescription(content: String)
    fun addImage(url: String)
    fun showSocialSharedNetworks(game: Game)
}