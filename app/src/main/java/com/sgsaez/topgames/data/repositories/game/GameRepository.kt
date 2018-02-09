package com.sgsaez.topgames.data.repositories.game

import com.sgsaez.topgames.data.persistence.entities.GameList
import io.reactivex.Single

interface GameRepository {
    fun getGames(initValue: String, query: String): Single<GameList>
}