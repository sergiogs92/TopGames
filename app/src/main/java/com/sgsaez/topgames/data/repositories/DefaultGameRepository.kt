package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.network.GameService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.presentation.model.GameList
import io.reactivex.Single
import io.reactivex.SingleEmitter

class DefaultGameRepository(private val gameService: GameService,
                            private val connectivityChecker: ConnectivityChecker) : GameRepository {

    override fun getGames(): Single<GameList> {
        return Single.create<GameList> { emitter: SingleEmitter<GameList> -> loadGames(emitter) }
    }

    private fun loadGames(emitter: SingleEmitter<GameList>) {
        try {
            val games = gameService.getGames().execute().body()
            if (games != null) emitter.onSuccess(games) else emitter.onError(Exception("No data received"))
        } catch (exception: Exception) {
            when {
                !connectivityChecker.isOnline() -> emitter.onError(Exception("No internet connection"))
                else -> emitter.onError(exception)
            }
        }
    }
}