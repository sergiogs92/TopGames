package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.network.GameService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.data.persistence.daos.GameDao
import com.sgsaez.topgames.data.persistence.entities.GameList
import io.reactivex.Single
import io.reactivex.SingleEmitter

class DefaultGameRepository(private val gameService: GameService,
                            private val gameDao: GameDao,
                            private val connectivityChecker: ConnectivityChecker) : GameRepository {

    override fun getGames(): Single<GameList> {
        return Single.create<GameList> { emitter: SingleEmitter<GameList> -> loadGames(emitter) }
    }

    private fun loadGames(emitter: SingleEmitter<GameList>) {
        try {
            val games = gameService.getGames().execute().body()
            if (games != null) {
                gameDao.insertAll(games.results)
                emitter.onSuccess(games)
            } else{
                emitter.onError(Exception("No data received"))
            }
        } catch (exception: Exception) {
            when {
                !connectivityChecker.isOnline() -> tryLoadOfflineGames(emitter)
                else -> emitter.onError(exception)
            }
        }
    }

    private fun tryLoadOfflineGames(emitter: SingleEmitter<GameList>){
        val games = gameDao.getGames()
        if(!games.isEmpty()){
            emitter.onSuccess(GameList(games))
        }else{
            emitter.onError(Exception("No internet connection"))
        }
    }
}