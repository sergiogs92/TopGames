package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.favourite.AddFavourite
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.support.domains.functional.fold
import com.sgsaez.topgames.support.fromHtml

class GameDetailPresenter(private val addFavourite: AddFavourite) : BasePresenter<GameDetailView>() {

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

    fun onSaveFavouriteGame(game: GameViewModel) {
        launchTask(action = { addFavourite.execute(game) },
                onCompleted = {
                    it.fold({ error -> error.toSaveFavouriteError() },
                            { view?.showSaveFavourite() })
                })
    }

    private fun FavoriteError.toSaveFavouriteError(): Unit? {
        return when (this) {
            FavoriteError.FavouriteAlreadyExist -> view?.showFavouriteAlreadyExists()
            else -> view?.showGeneralError()
        }
    }

}