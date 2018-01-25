package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.persistence.entities.GameList
import io.reactivex.Single

interface GameRepository {
    fun getGames(query: String): Single<GameList>
}