package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.persistence.entities.Game
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.support.domains.functional.Either

class DefaultGameRepositoryMock : GameRepository {

    override fun getGames(initValue: String, query: String): Either<GamesException, GameList> {
        val gameList = GameList(getMockGameList())
        return Either.Right(gameList)
    }

    private fun getMockGameList(): List<Game> = (1..10).map {
        val number = +it
        val url = "goo.gl/svPzkf"
        Game(it.toString(), "This is the game $number", "Game $number", Image(url))
    }

}