package com.sgsaez.topgames.data.repositories.game

import com.sgsaez.topgames.data.network.ApiService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.data.persistence.daos.GameDao
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.support.domains.functional.Either

class DefaultGameRepository(private val gameService: ApiService, private val gameDao: GameDao,
                            private val connectivityChecker: ConnectivityChecker) : GameRepository {

    override fun getGames(initValue: String, query: String): Either<GamesException, GameList> {
        return try {
            when {
                query.isEmpty() -> {
                    val games = gameService.getGames(initValue).execute().body()
                    saveGamesReceived(games)
                }
                else -> tryLoadOfflineSearch(query)
            }
        } catch (exception: Exception) {
            when {
                !connectivityChecker.isOnline() -> tryLoadOfflineGames()
                else -> Either.Left(GamesException(GameError.DEFAULT))
            }
        }
    }

    private fun saveGamesReceived(games: GameList?): Either<GamesException, GameList> {
        return games?.let {
            when {
                !games.results.isEmpty() -> {
                    gameDao.insertAll(games.results);Either.Right(games)
                }
                else -> Either.Left(GamesException(GameError.ERROR_NO_DATA_RECEIVED))
            }
        } ?: Either.Left(GamesException(GameError.DEFAULT))
    }

    private fun tryLoadOfflineGames(): Either<GamesException, GameList> {
        val games = gameDao.getGames()
        return if (games.isNotEmpty()) Either.Right(GameList(games))
        else Either.Left(GamesException(GameError.ERROR_INTERNET_CONNECTION))
    }

    private fun tryLoadOfflineSearch(query: String): Either<GamesException, GameList> {
        val games = gameDao.searchGames(query.plus("%"))
        return if (games.isNotEmpty()) Either.Right(GameList(games))
        else Either.Left(GamesException(GameError.ERROR_NO_DATA_FOUND))
    }

}