package com.sgsaez.topgames.domain.game

import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.support.domains.functional.Either
import com.sgsaez.topgames.support.domains.functional.fold

class GetGames(private val gameRepository: GameRepository) {

    fun execute(initValue: String, query: String): Either<GamesException, List<GameViewModel>> {
        return gameRepository.getGames(initValue, query)
                .let {
                    it.fold(
                            { error -> Either.Left(error) },
                            { gameList -> getGameViewModelList(gameList).let { Either.Right(it) } })
                }
    }

    private fun getGameViewModelList(games: GameList): List<GameViewModel> {
        return games.results.map {
            val description = if (it.description.isNullOrBlank()) "No description" else it.description!!
            GameViewModel(it.id, description, it.name, it.image.url)
        }
    }

}
