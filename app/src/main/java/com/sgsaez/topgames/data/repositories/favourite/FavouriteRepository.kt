package com.sgsaez.topgames.data.repositories.favourite

import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.support.domains.functional.Either

interface FavouriteRepository {
    fun getFavorites(): Either<FavoriteError, FavouriteList>
    fun addFavorite(favourite: Favourite): Either<FavoriteError, Unit>
    fun removeFavorite(favourite: Favourite): Either<FavoriteError, Unit>
}