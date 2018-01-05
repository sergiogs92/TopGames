package com.sgsaez.topgames.data.network

import com.sgsaez.topgames.presentation.model.GameList
import retrofit2.Call
import retrofit2.http.GET

interface GameService {
    @GET("?api_key=8d352a71967d4946a2021da547e6c31cc6256241&limit=10&format=json")
    fun getGames() : Call<GameList>
}