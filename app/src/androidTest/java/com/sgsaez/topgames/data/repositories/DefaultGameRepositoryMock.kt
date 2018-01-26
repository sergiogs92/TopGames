package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.persistence.entities.EGame
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.presentation.model.Image
import io.reactivex.Single
import io.reactivex.SingleEmitter

class DefaultGameRepositoryMock : GameRepository {

    override fun getGames(query: String): Single<GameList> {
        return Single.create<GameList> { emitter: SingleEmitter<GameList> ->
            val gameList = GameList(getMockGameList())
            emitter.onSuccess(gameList)
        }
    }

    private fun getMockGameList(): List<EGame> = (1..10).map {
        val number = +it
        val url = "goo.gl/svPzkf"
        EGame(it.toString(), "This is the game $number", "Game $number", Image(url))
    }
}