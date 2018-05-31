package com.sgsaez.topgames.data.repositories.game

import com.sgsaez.topgames.data.network.ApiService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.data.persistence.daos.GameDao
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.domain.game.GamesException.Companion.DEFAULT
import com.sgsaez.topgames.domain.game.GamesException.Companion.ERROR_INTERNET_CONNECTION
import com.sgsaez.topgames.domain.game.GamesException.Companion.ERROR_NO_DATA_FOUND
import com.sgsaez.topgames.domain.game.GamesException.Companion.ERROR_NO_DATA_RECEIVED
import com.sgsaez.topgames.utils.condition
import io.reactivex.Single
import io.reactivex.SingleEmitter

class DefaultGameRepository(private val gameService: ApiService, private val gameDao: GameDao,
                            private val connectivityChecker: ConnectivityChecker) : GameRepository {

    override fun getGames(query: String): Single<GameList> {
        return Single.create<GameList> { emitter: SingleEmitter<GameList> -> loadGames(query, emitter) }
    }

    private fun loadGames(query: String, emitter: SingleEmitter<GameList>) {
        try {
            condition({ query.isEmpty() },
                    {
                        val games = gameService.getGames().execute().body()
                        games?.let {
                            saveGamesReceived(it, emitter)
                        }
                    }, { tryLoadOfflineSearch(query, emitter) })
        } catch (exception: Exception) {
            when {
                !connectivityChecker.isOnline() -> tryLoadOfflineGames(emitter)
                else -> emitter.onError(GamesException(DEFAULT))
            }
        }
    }

    private fun saveGamesReceived(games: GameList, emitter: SingleEmitter<GameList>) {
        condition({ !games.results.isEmpty() },
                {
                    gameDao.insertAll(games.results)
                    emitter.onSuccess(games)
                }, { emitter.onError(GamesException(ERROR_NO_DATA_RECEIVED)) })
    }

    private fun tryLoadOfflineGames(emitter: SingleEmitter<GameList>) {
        val games = gameDao.getGames()
        condition({ !games.isEmpty() }, { emitter.onSuccess(GameList(games)) }, { emitter.onError(GamesException(ERROR_INTERNET_CONNECTION)) })
    }

    private fun tryLoadOfflineSearch(query: String, emitter: SingleEmitter<GameList>) {
        val games = gameDao.searchGames(query.plus("%"))
        condition({ !games.isEmpty() }, { emitter.onSuccess(GameList(games)) }, { emitter.onError(GamesException(ERROR_NO_DATA_FOUND)) })
    }
}