package com.sgsaez.topgames.domain.favourite

import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.BaseCase
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.support.SchedulerProvider
import com.sgsaez.topgames.support.domains.functional.Either

class RemoveFavourite(private val favouriteRepository: FavouriteRepository, schedulerProvider: SchedulerProvider) : BaseCase(schedulerProvider) {

    fun execute(favouriteGame: GameViewModel, onCompleted: (Either<FavouritesException, Unit>) -> Unit) {
        asyncExecute {
            val removeFavorite = favouriteRepository.removeFavorite(favouriteGame.let { Favourite(it.id, it.name, it.description, Image(it.imageUrl)) })
            uiExecute {
                onCompleted(removeFavorite)
            }
        }
    }

}