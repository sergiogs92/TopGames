package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import io.reactivex.Single
import io.reactivex.SingleEmitter

class DefaultFavouriteRepositoryMock : FavouriteRepository {

    override fun getFavorites(): Single<FavouriteList> {
        return Single.create<FavouriteList> { emitter: SingleEmitter<FavouriteList> ->
            val gameList = FavouriteList(getMockFavouriteList())
            emitter.onSuccess(gameList)
        }
    }

    private fun getMockFavouriteList(): List<Favourite> = (1..10).map {
        val number = +it
        val url = "goo.gl/svPzkf"
        Favourite(it.toString(), "This is the game $number", "Game $number", Image(url))
    }

    override fun addFavorite(favourite: Favourite): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> ->
            emitter.onSuccess(Unit)
        }
    }

    override fun removeFavorite(favourite: Favourite): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> ->
            emitter.onSuccess(Unit)
        }
    }
}