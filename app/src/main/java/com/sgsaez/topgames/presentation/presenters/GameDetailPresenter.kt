package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.fromHtml

class GameDetailPresenter : BasePresenter<GameDetailView>() {

    fun onInit(game: Game) {
        view?.addTitleToolbar(game.name)
        view?.addDescription(convertFromHtml(game.description))
        view?.addImage(game.image.url)
    }

    private fun convertFromHtml(description: String) = description.fromHtml().toString()

    fun onSocialSharedClicked(game: Game) {
        view?.showSocialSharedNetworks(game)
    }

    fun onResetStatusBarColor() {
        view?.resetStatusBarColor()
    }
}