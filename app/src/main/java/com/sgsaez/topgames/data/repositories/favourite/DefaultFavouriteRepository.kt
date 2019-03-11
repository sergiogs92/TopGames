package com.sgsaez.topgames.data.repositories.favourite

import com.sgsaez.topgames.data.persistence.daos.FavouriteDao
import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.support.domains.functional.Either

class DefaultFavouriteRepository(private val favouriteDao: FavouriteDao) : FavouriteRepository {

    override fun getFavorites(): Either<FavoriteError, FavouriteList> {
        return try {
            favouriteDao.getFavourites()
                    .takeIf { it.isNotEmpty() }
                    ?.let { Either.Right(FavouriteList(it)) }
                    ?: Either.Left(FavoriteError.FavouritesNotFound)
        } catch (exception: Exception) {
            Either.Left(FavoriteError.UnexpectedError)
        }
    }

    override fun addFavorite(favourite: Favourite): Either<FavoriteError, Unit> {
        return try {
            favouriteDao.getFavourite(favourite.id)
                    ?.let { Either.Left(FavoriteError.FavouriteAlreadyExist) }
                    ?: run { favouriteDao.insertAll(favourite).let { Either.Right(Unit) } }
        } catch (exception: Exception) {
            Either.Left(FavoriteError.UnexpectedError)
        }
    }

    override fun removeFavorite(favourite: Favourite): Either<FavoriteError, Unit> {
        return try {
            favouriteDao.deleteFavourite(favourite).let { Either.Right(Unit) }
        } catch (exception: Exception) {
            Either.Left(FavoriteError.UnexpectedError)
        }
    }

}