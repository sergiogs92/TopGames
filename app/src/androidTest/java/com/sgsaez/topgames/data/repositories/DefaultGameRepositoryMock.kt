package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.data.repositories.game.NetGame
import com.sgsaez.topgames.data.repositories.game.NetGameList
import com.sgsaez.topgames.data.repositories.game.NetImage
import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.support.domains.functional.Either

class DefaultGameRepositoryMock : GameRepository {
    override fun getGames(requestPage: String, query: String): Either<GameError, NetGameList> {
        val gameList = getMockGameList()
        return Either.Right(gameList)
    }

    private fun getMockGameList(): NetGameList = NetGameList((1..10).map {
        val number = +it
        val url = "goo.gl/svPzkf"
        NetGame(it.toString(), "This is the game $number", "Game $number", NetImage(url))
    })

}