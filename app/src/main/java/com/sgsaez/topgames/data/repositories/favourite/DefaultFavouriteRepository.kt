package com.sgsaez.topgames.data.repositories.favourite

import com.sgsaez.topgames.data.persistence.daos.FavouriteDao
import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.support.domains.functional.Either

class DefaultFavouriteRepository(private val favouriteDao: FavouriteDao) : FavouriteRepository {

    override fun getFavorites(): Either<FavouritesException, FavouriteList> {
        return try {
            val favourites = favouriteDao.getFavourites()
            if(favourites.isNotEmpty()) Either.Right(FavouriteList(favourites))
            else Either.Left(FavouritesException(FavoriteError.ERROR_NO_DATA_FOUND))
        } catch (exception: Exception) {
            Either.Left(FavouritesException(FavoriteError.DEFAULT))
        }
    }

    override fun addFavorite(favourite: Favourite): Either<FavouritesException, Unit> {
        return try {
            val favouriteToFind = favouriteDao.getFavourite(favourite.id)
            favouriteToFind?.let {
                Either.Left(FavouritesException(FavoriteError.FAVOURITE_ALREADY_EXITS))
            } ?: run {
                favouriteDao.insertAll(favourite); Either.Right(Unit)
            }
        } catch (exception: Exception) {
            Either.Left(FavouritesException(FavoriteError.FAVOURITE_ALREADY_EXITS))
        }
    }

    override fun removeFavorite(favourite: Favourite): Either<FavouritesException, Unit> {
        return try {
            favouriteDao.deleteFavourite(favourite)
            Either.Right(Unit)
        } catch (exception: Exception) {
            Either.Left(FavouritesException(FavoriteError.DEFAULT))
        }
    }

}