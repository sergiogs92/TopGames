package com.sgsaez.topgames.data.repositories.game

import com.sgsaez.topgames.data.network.ApiService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.data.persistence.daos.GameDao
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.support.domains.functional.Either

class DefaultGameRepository(private val gameService: ApiService, private val gameDao: GameDao,
                            private val connectivityChecker: ConnectivityChecker) : GameRepository {

    override fun getGames(requestPage: String, query: String): Either<GameError, GameList> {
        return try {
            if (query.isEmpty()) gameService.getGames(requestPage).execute().body().let(::saveGamesReceived)
            else tryLoadOfflineSearch(query)
        } catch (exception: Exception) {
            if (!connectivityChecker.isOnline()) tryLoadOfflineGames()
            else Either.Left(GameError.UnexpectedError)
        }
    }

    private fun saveGamesReceived(games: GameList?): Either<GameError, GameList> {
        return games
                ?.takeIf { it.results.isNotEmpty() }
                ?.let { gameDao.insertAll(games.results).let { Either.Right(games) } }
                ?: Either.Left(GameError.GamesNotReceived)
    }

    private fun tryLoadOfflineGames(): Either<GameError, GameList> {
        return gameDao.getGames()
                .takeIf { it.isNotEmpty() }
                ?.let { Either.Right(GameList(it)) }
                ?: Either.Left(GameError.NetworkConnection)
    }

    private fun tryLoadOfflineSearch(query: String): Either<GameError, GameList> {
        return gameDao.searchGames(query.plus("%"))
                .takeIf { it.isNotEmpty() }
                ?.let { Either.Right(GameList(it)) }
                ?: Either.Left(GameError.GamesNotFound)
    }

}