package com.sgsaez.topgames.domain.game

import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.data.repositories.game.NetGameList
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.support.domains.functional.Either
import com.sgsaez.topgames.support.domains.functional.fold

class GetGames(private val gameRepository: GameRepository) {

    fun execute(initValue: String, query: String): Either<GameError, List<Game>> {
        return gameRepository.getGames(initValue, query)
                .let {
                    it.fold(
                            { error -> Either.Left(error) },
                            { gameList -> getGameViewModelList(gameList).let { Either.Right(it) } })
                }
    }

    private fun getGameViewModelList(netGames: NetGameList): List<Game> {
        return netGames.results.map {
            val description = if (it.description.isNullOrBlank()) "No description" else it.description
            Game(it.id, description, it.name, it.image.url)
        }
    }

}
