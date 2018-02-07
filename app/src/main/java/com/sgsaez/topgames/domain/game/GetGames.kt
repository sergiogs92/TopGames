package com.sgsaez.topgames.domain.game

import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.presentation.model.GameViewModel
import io.reactivex.Single

class GetGames(private val gameRepository: GameRepository) {

    fun execute(query: String): Single<List<GameViewModel>> {
        val games = gameRepository.getGames(query)
        return games.map { gameList: GameList? ->
            val items = gameList?.results ?: emptyList()
            items.map { GameViewModel(it.id, if (it.description.isNullOrBlank()) "No description" else it.description!!, it.name, it.image.url) }
        }
    }
}