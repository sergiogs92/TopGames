package com.sgsaez.topgames.utils

import com.google.gson.Gson

object NotParcelled {

    private val gson = Gson()

    fun <T> toNotParcelled(t: T): String {
        return gson.toJson(t)
    }

    fun <T> fromNotParcelled(notParcelled: String, t: Class<T>): T {
        return gson.fromJson(notParcelled, t)
    }
}