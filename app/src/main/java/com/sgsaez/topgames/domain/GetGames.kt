package com.sgsaez.topgames.domain

import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.data.repositories.GameRepository
import com.sgsaez.topgames.presentation.model.Game
import io.reactivex.Single

class GetGames(private val gameRepository: GameRepository) {

    fun execute(): Single<List<Game>> {
        val games = gameRepository.getGames()
        return games.map { gameList: GameList? ->
            val items = gameList?.results ?: emptyList()
            items.map { Game(it.id, it.description, it.name, it.image) }
        }
    }
}