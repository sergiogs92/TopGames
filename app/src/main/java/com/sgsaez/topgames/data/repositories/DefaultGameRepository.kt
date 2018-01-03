package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.service.GameService
import com.sgsaez.topgames.presentation.model.GameList
import io.reactivex.Single
import io.reactivex.SingleEmitter

class DefaultGameRepository(private val gameService: GameService) : GameRepository {

    override fun getGames(): Single<GameList> {
        return Single.create<GameList> { emitter: SingleEmitter<GameList> ->
            loadGames(emitter)
        }
    }

    private fun loadGames(emitter: SingleEmitter<GameList>) = try {
        val games = gameService.getGames().execute().body()
        if (games != null) emitter.onSuccess(games) else emitter.onError(Exception("No data received"))
    } catch (exception: Exception) {
        emitter.onError(exception)
    }
}