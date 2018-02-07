package com.sgsaez.topgames.data.repositories.favourite

import com.sgsaez.topgames.data.persistence.daos.FavouriteDao
import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException.Companion.ERROR_NO_DATA_FOUND
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException.Companion.FAVOURITE_ALREADY_EXITS
import io.reactivex.Single
import io.reactivex.SingleEmitter

class DefaultFavouriteRepository(private val favouriteDao: FavouriteDao) : FavouriteRepository {

    override fun getFavorites(): Single<FavouriteList> {
        return Single.create<FavouriteList> { emitter: SingleEmitter<FavouriteList> -> retrieveFavourites(emitter) }
    }

    private fun retrieveFavourites(emitter: SingleEmitter<FavouriteList>) {
        val favourites = favouriteDao.getFavourites()
        if (favourites.isNotEmpty()) {
            emitter.onSuccess(FavouriteList(favourites))
        } else {
            emitter.onError(FavouritesException(ERROR_NO_DATA_FOUND))
        }
    }

    override fun addFavorite(favourite: Favourite): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> -> tryInsertFavourite(favourite, emitter) }
    }

    private fun tryInsertFavourite(favourite: Favourite, emitter: SingleEmitter<Unit>) {
        val favouriteToFind = favouriteDao.getFavourite(favourite.id)
        favouriteToFind?.let { emitter.onError(FavouritesException(FAVOURITE_ALREADY_EXITS)) } ?: insertFavourite(favourite, emitter)
    }

    private fun insertFavourite(favourite: Favourite, emitter: SingleEmitter<Unit>) {
        favouriteDao.insertAll(favourite).let { emitter.onSuccess(Unit) }
    }

    override fun removeFavorite(favourite: Favourite): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> -> deleteFavourite(favourite, emitter) }
    }

    private fun deleteFavourite(favourite: Favourite, emitter: SingleEmitter<Unit>) {
        favouriteDao.deleteFavourite(favourite).let { emitter.onSuccess(Unit) }
    }

}