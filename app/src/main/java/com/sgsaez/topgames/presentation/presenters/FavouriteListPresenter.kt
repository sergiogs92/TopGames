package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.favourite.GetFavourites
import com.sgsaez.topgames.domain.favourite.RemoveFavourite
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.FavouriteListView
import com.sgsaez.topgames.support.domains.functional.fold

class FavouriteListPresenter(private val getFavourites: GetFavourites, private val removeFavourite: RemoveFavourite) : BasePresenter<FavouriteListView>() {

    fun onLoadFavourites() {
        getFavourites.execute(onCompleted = { result ->
            result.fold(
                    { error -> error.toGetFavouritesThrowable() },
                    { games -> view?.addFavouriteToList(games) })
        })
    }

    private fun FavouritesException.toGetFavouritesThrowable(): Unit? {
        return when (error) {
            FavoriteError.ERROR_NO_DATA_FOUND -> view?.showNoDataFoundError()
            else -> view?.showNoDataFoundError()
        }
    }

    fun onFavouriteClicked(favourite: GameViewModel) {
        view?.navigateToGame(favourite)
    }

    fun onRemoveFavouriteGame(favourite: GameViewModel) {
        removeFavourite.execute(favourite, onCompleted = { result ->
            result.fold(
                    { error -> error.toGetFavouritesThrowable() },
                    { view?.removeFavouriteToList(favourite) })
        })
    }

}