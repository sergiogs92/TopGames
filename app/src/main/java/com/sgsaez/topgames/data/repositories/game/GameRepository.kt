package com.sgsaez.topgames.data.repositories.game

import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.support.domains.functional.Either

interface GameRepository {
    fun getGames(requestPage: String, query: String): Either<GamesException, GameList>
}