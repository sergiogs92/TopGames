package com.sgsaez.topgames.domain.favourite

import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.support.domains.functional.Either

class AddFavourite(private val favouriteRepository: FavouriteRepository) {

    fun execute(favouriteGame: Game): Either<FavoriteError, Unit> {
        val favourite = Favourite(favouriteGame.id, favouriteGame.name, favouriteGame.description, Image(favouriteGame.imageUrl))
        return favouriteRepository.addFavorite(favourite)
    }

}