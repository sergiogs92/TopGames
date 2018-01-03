package com.sgsaez.topgames.data.service

import com.sgsaez.topgames.presentation.model.GameList
import retrofit2.Call
import retrofit2.http.GET

interface GameService {
    @GET("/?api_key=YOUR_API_KEY&limit=5&format=json")
    fun getGames() : Call<GameList>
}