package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.support.domains.functional.Either

class DefaultFavouriteRepositoryMock : FavouriteRepository {

    override fun getFavorites(): Either<FavouritesException, FavouriteList>{
        val gameList = FavouriteList(getMockFavouriteList())
        return Either.Right(gameList)
    }

    private fun getMockFavouriteList(): List<Favourite> = (1..10).map {
        val number = +it
        val url = "goo.gl/svPzkf"
        Favourite(it.toString(), "This is the game $number", "Game $number", Image(url))
    }

    override fun addFavorite(favourite: Favourite): Either<FavouritesException, Unit> {
        return Either.Right(Unit)
    }

    override fun removeFavorite(favourite: Favourite): Either<FavouritesException, Unit> {
        return Either.Right(Unit)
    }

}