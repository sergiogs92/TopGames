package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.favourite.GetFavourites
import com.sgsaez.topgames.domain.favourite.RemoveFavourite
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.FavouriteListView
import com.sgsaez.topgames.support.SchedulerProvider

class FavouriteListPresenter(private val getFavourites: GetFavourites, private val removeFavourite: RemoveFavourite,
                             private val schedulerProvider: SchedulerProvider) : BasePresenter<FavouriteListView>() {

    fun onLoadFavourites() {
        addDisposable(getFavourites.execute()
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .subscribe({ view?.addFavouriteToList(it) }, { it.toGetFavouritesThrowable() }))
    }

    private fun Throwable.toGetFavouritesThrowable(): Unit? {
        return when {
            this is FavouritesException -> when (error) {
                FavoriteError.ERROR_NO_DATA_FOUND -> view?.showNoDataFoundError()
                else -> view?.showNoDataFoundError()
            }
            else -> view?.showNoDataFoundError()
        }
    }

    fun onFavouriteClicked(favourite: GameViewModel) {
        view?.navigateToGame(favourite)
    }

    fun onRemoveFavouriteGame(favourite: GameViewModel) {
        addDisposable(removeFavourite.execute(favourite)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .subscribe { _ -> view?.removeFavouriteToList(favourite) })
    }

}