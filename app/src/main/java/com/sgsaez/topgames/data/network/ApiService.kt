package com.sgsaez.topgames.data.network

import com.sgsaez.topgames.data.persistence.entities.GameList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("?api_key=8d352a71967d4946a2021da547e6c31cc6256241&limit=20&format=json")
    fun getGames(@Query("offset") initValue: String): Call<GameList>
}
