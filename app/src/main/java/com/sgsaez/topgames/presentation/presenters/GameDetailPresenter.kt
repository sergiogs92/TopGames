package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.favourite.AddFavourite
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.SchedulerProvider
import com.sgsaez.topgames.utils.fromHtml

class GameDetailPresenter(private val addFavourite: AddFavourite, private val schedulerProvider: SchedulerProvider) :
        BasePresenter<GameDetailView>() {

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
        addDisposable(addFavourite.execute(game)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .subscribe({ _ ->
                    view?.showSaveFavourite()
                }, {
                    val favouritesException = it as FavouritesException
                    when (favouritesException.error) {
                        FavoriteError.FAVOURITE_ALREADY_EXITS -> view?.showFavouriteAlreadyExists()
                        else -> view?.showGeneralError()
                    }
                }))
    }

}