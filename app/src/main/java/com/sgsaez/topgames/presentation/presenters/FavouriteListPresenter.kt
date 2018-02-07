package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.favourite.GetFavourites
import com.sgsaez.topgames.domain.favourite.RemoveFavourite
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException.Companion.ERROR_NO_DATA_FOUND
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.view.FavouriteListView
import com.sgsaez.topgames.utils.SchedulerProvider

class FavouriteListPresenter(private val getFavourites: GetFavourites, private val removeFavourite: RemoveFavourite,
                             private val schedulerProvider: SchedulerProvider) : BasePresenter<FavouriteListView>() {

    fun onLoadFavourites() {
        getFavourites.execute()
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .subscribe({ favourites ->
                    view?.addFavouriteToList(favourites)
                }, {
                    val favouritesException = it as FavouritesException
                    when (favouritesException.tag) {
                        ERROR_NO_DATA_FOUND -> view?.showNoDataFoundError()
                    }
                })
    }

    fun onFavouriteClicked(favourite: GameViewModel) {
        view?.navigateToGame(favourite)
    }

    fun onRemoveFavouriteGame(favourite: GameViewModel) {
        removeFavourite.execute(favourite)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .subscribe({ _ ->
                    view?.removeFavouriteToList(favourite)
                })
    }
}