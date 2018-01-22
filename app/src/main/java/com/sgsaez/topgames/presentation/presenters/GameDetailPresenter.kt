package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.fromHtml

class GameDetailPresenter : BasePresenter<GameDetailView>() {

    fun onInit(noDescription: String, game: Game) {
        view?.addTitleToolbar(game.name)
        view?.addDescription(convertFromHtml(noDescription, game.description))
        view?.addImage(game.image.url)
    }

    private fun convertFromHtml(noDescription: String, description: String?) = description?.fromHtml()?.toString() ?: noDescription

    fun onSocialSharedClicked(game: Game) {
        view?.showSocialSharedNetworks(game)
    }
}