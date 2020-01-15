package com.sgsaez.topgames.domain.favourite

import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.support.domains.functional.Either
import com.sgsaez.topgames.support.domains.functional.fold

class GetFavourites(private val favouriteRepository: FavouriteRepository) {

    fun execute(): Either<FavoriteError, List<Game>> {
        return favouriteRepository.getFavorites()
                .let {
                    it.fold({ error -> Either.Left(error) },
                            { favouriteList -> getGameViewModelList(favouriteList) })
                }
    }

    private fun getGameViewModelList(games: FavouriteList): Either<FavoriteError, List<Game>> {
        return games.results
                .map { Game(it.id, it.description, it.name, it.image.url) }
                .takeIf { it.isNotEmpty() }
                ?.let { Either.Right(it) }
                ?: Either.Left(FavoriteError.FavouritesNotFound)
    }

}