package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.network.GameService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.data.persistence.daos.GameDao
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.domain.ErrorConstants
import com.sgsaez.topgames.domain.game.GamesException
import io.reactivex.Single
import io.reactivex.SingleEmitter

class DefaultGameRepository(private val gameService: GameService, private val gameDao: GameDao,
                            private val connectivityChecker: ConnectivityChecker) : GameRepository {

    override fun getGames(query: String): Single<GameList> {
        return Single.create<GameList> { emitter: SingleEmitter<GameList> -> loadGames(query, emitter) }
    }

    private fun loadGames(query: String, emitter: SingleEmitter<GameList>) {
        try {
            if (query.isEmpty()) {
                val games = gameService.getGames().execute().body()
                games?.let { saveGamesReceived(it, emitter) }
            } else {
                tryLoadOfflineSearch(query, emitter)
            }
        } catch (exception: Exception) {
            when {
                !connectivityChecker.isOnline() -> tryLoadOfflineGames(emitter)
                else -> emitter.onError(GamesException(ErrorConstants.DEFAULT))
            }
        }
    }

    private fun saveGamesReceived(games: GameList, emitter: SingleEmitter<GameList>) {
        if (!games.results.isEmpty()) {
            gameDao.insertAll(games.results)
            emitter.onSuccess(games)
        } else {
            emitter.onError(GamesException(ErrorConstants.ERROR_NO_DATA_RECEIVED))
        }
    }

    private fun tryLoadOfflineGames(emitter: SingleEmitter<GameList>) {
        val games = gameDao.getGames()
        if (!games.isEmpty()) {
            emitter.onSuccess(GameList(games))
        } else {
            emitter.onError(GamesException(ErrorConstants.ERROR_INTERNET_CONNECTION))
        }
    }

    private fun tryLoadOfflineSearch(query: String, emitter: SingleEmitter<GameList>) {
        val games = gameDao.searchGames(query.plus("%"))
        if (!games.isEmpty()) {
            emitter.onSuccess(GameList(games))
        } else {
            emitter.onError(GamesException(ErrorConstants.ERROR_NO_DATA_FOUND))
        }
    }
}