package com.sgsaez.topgames.data.repositories.game

import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.support.domains.functional.Either

interface GameRepository {
    fun getGames(requestPage: String, query: String): Either<GameError, NetGameList>
}