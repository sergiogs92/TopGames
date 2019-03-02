package com.sgsaez.topgames.domain.game

import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.support.domains.functional.Either
import com.sgsaez.topgames.support.domains.functional.fold

class GetGames(private val gameRepository: GameRepository) {

    fun execute(initValue: String, query: String): Either<GamesException, List<GameViewModel>> {
        val games = gameRepository.getGames(initValue, query)
        val gamesViewModel = games.fold(
                { error -> Either.Left(error) },
                { gameList ->
                    val gameViewModelList = getGameViewModelList(gameList)
                    Either.Right(gameViewModelList)
                }
        )
        return gamesViewModel
    }

    private fun getGameViewModelList(games: GameList): List<GameViewModel> {
        return games.results.map {
            val description = if (it.description.isNullOrBlank()) "No description" else it.description!!
            GameViewModel(it.id, description, it.name, it.image.url)
        }
    }

}
