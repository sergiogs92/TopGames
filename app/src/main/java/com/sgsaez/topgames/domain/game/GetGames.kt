package com.sgsaez.topgames.domain.game

import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.utils.condition
import io.reactivex.Single

class GetGames(private val gameRepository: GameRepository) {

    fun execute(initValue: String, query: String): Single<List<GameViewModel>> {
        val games = gameRepository.getGames(initValue, query)
        return games.map { gameList: GameList? ->
            val items = gameList?.results ?: emptyList()
            items.map {
                val description =  if(it.description.isNullOrBlank()) "No description" else it.description!!
                GameViewModel(it.id, description, it.name, it.image.url)
            }
        }
    }

}