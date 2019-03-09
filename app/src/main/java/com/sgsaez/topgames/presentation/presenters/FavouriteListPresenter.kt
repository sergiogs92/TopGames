package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.favourite.GetFavourites
import com.sgsaez.topgames.domain.favourite.RemoveFavourite
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.presentation.model.GameViewModel
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

    private fun FavouritesException.toGetFavouriteError(): Unit? {
        return when (error) {
            FavoriteError.ERROR_NO_DATA_FOUND -> view?.showNoDataFoundError()
            else -> view?.showNoDataFoundError()
        }
    }

    fun onFavouriteClicked(favourite: GameViewModel) {
        view?.navigateToGame(favourite)
    }

    fun onRemoveFavouriteGame(favourite: GameViewModel) {
        launchTask(action = { removeFavourite.execute(favourite) },
                onCompleted = {
                    it.fold({ error -> error.toGetFavouriteError() },
                            { view?.removeFavouriteToList(favourite) })
                })
    }

}