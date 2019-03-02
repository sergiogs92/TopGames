package com.sgsaez.topgames.domain.favourite

import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.support.domains.functional.Either
import com.sgsaez.topgames.support.domains.functional.fold

class GetFavourites(private val favouriteRepository: FavouriteRepository) {

    fun execute(): Either<FavouritesException, List<GameViewModel>> {
        val favouriteGames = favouriteRepository.getFavorites()
        val gamesViewModel = favouriteGames.fold(
                { error -> Either.Left(error) },
                { favouriteList -> getGameViewModelList(favouriteList) }
        )
        return gamesViewModel
    }

    private fun getGameViewModelList(games: FavouriteList): Either<FavouritesException, List<GameViewModel>> {
        val gameViewModelList = games.results.map { GameViewModel(it.id, it.description, it.name, it.image.url) }
        return if (gameViewModelList.isEmpty()) Either.Left(FavouritesException(FavoriteError.ERROR_NO_DATA_FOUND))
        else Either.Right(gameViewModelList)
    }

}