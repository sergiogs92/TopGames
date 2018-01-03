package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.presentation.model.GameList
import io.reactivex.Single

interface GameRepository {
    fun getGames(): Single<GameList>
}