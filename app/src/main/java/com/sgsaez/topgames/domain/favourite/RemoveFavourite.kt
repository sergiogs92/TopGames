package com.sgsaez.topgames.domain.favourite

import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.presentation.model.GameViewModel
import io.reactivex.Single

class RemoveFavourite(private val favouriteRepository: FavouriteRepository) {

    fun execute(favouriteGame: GameViewModel): Single<Unit> {
        return favouriteRepository.removeFavorite(favouriteGame.let { Favourite(it.id, it.name, it.description, Image(it.imageUrl)) })
    }
}