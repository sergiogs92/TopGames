package com.sgsaez.topgames.data.repositories.game

import com.sgsaez.topgames.data.network.ApiService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.support.domains.functional.Either

class DefaultGameRepository(private val gameService: ApiService,
                            private val connectivityChecker: ConnectivityChecker) : GameRepository {

    override fun getGames(requestPage: String, query: String): Either<GameError, NetGameList> {
        return try {
            gameService.getGames(requestPage).execute().body().let(::onCompletedGetGames)
        } catch (exception: Exception) {
            if (!connectivityChecker.isOnline()) Either.Left(GameError.NetworkConnection)
            else Either.Left(GameError.UnexpectedError)
        }
    }

    private fun onCompletedGetGames(games: NetGameList?): Either<GameError, NetGameList> {
        return games
                ?.takeIf { it.results.isNotEmpty() }
                ?.let { Either.Right(games) }
                ?: Either.Left(GameError.GamesNotReceived)
    }

}
