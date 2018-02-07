package com.sgsaez.topgames.domain.favourite

import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.presentation.model.GameViewModel
import io.reactivex.Single

class GetFavourites(private val favouriteRepository: FavouriteRepository) {

    fun execute(): Single<List<GameViewModel>> {
        val favouriteGames = favouriteRepository.getFavorites()
        return favouriteGames.map { favouriteList: FavouriteList? ->
            val items = favouriteList?.results ?: emptyList()
            items.map { GameViewModel(it.id, it.description, it.name, it.image.url) }
        }
    }

}