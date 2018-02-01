package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.fromHtml

class GameDetailPresenter : BasePresenter<GameDetailView>() {

    fun onInit(game: GameViewModel) {
        view?.addTitleToolbar(game.name)
        view?.addDescription(convertFromHtml(game.description))
        view?.addImage(game.imageUrl)
    }

    private fun convertFromHtml(description: String) = description.fromHtml().toString()

    fun onSocialSharedClicked(game: GameViewModel) {
        view?.showSocialSharedNetworks(game)
    }

    fun onResetStatusBarColor() {
        view?.resetStatusBarColor()
    }
}