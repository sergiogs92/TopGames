package com.sgsaez.topgames.data.repositories.game

import com.google.gson.annotations.SerializedName

data class NetGameList(val results: List<NetGame>)

data class NetGame(val id: String, val description: String?, val name: String, val image: NetImage)

data class NetImage(@SerializedName("screen_url") var url: String)