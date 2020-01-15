package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.favourite.GetFavourites
import com.sgsaez.topgames.domain.favourite.RemoveFavourite
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.view.FavouriteListView
import com.sgsaez.topgames.support.domains.functional.fold

class FavouriteListPresenter(private val getFavourites: GetFavourites,
                             private val removeFavourite: RemoveFavourite) : BasePresenter<FavouriteListView>() {

    fun onLoadFavourites() {
        launchTask(action = { getFavourites.execute() },
                onCompleted = {
                    it.fold({ error -> error.toGetFavouriteError() },
                            { games -> view?.addFavouriteToList(games) })
                })
    }

    private fun FavoriteError.toGetFavouriteError(): Unit? {
        return when (this) {
            FavoriteError.FavouritesNotFound -> view?.showNoDataFoundError()
            else -> view?.showNoDataFoundError()
        }
    }

    fun onFavouriteClicked(favourite: Game) {
        view?.navigateToGame(favourite)
    }

    fun onRemoveFavouriteGame(favourite: Game) {
        launchTask(action = { removeFavourite.execute(favourite) },
                onCompleted = {
                    it.fold({ error -> error.toGetFavouriteError() },
                            { view?.removeFavouriteToList(favourite) })
                })
    }

}