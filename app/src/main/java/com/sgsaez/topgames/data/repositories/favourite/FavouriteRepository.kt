package com.sgsaez.topgames.data.repositories.favourite

import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import io.reactivex.Single

interface FavouriteRepository {
    fun getFavorites(): Single<FavouriteList>
    fun addFavorite(favourite: Favourite): Single<Unit>
    fun removeFavorite(favourite: Favourite): Single<Unit>
}