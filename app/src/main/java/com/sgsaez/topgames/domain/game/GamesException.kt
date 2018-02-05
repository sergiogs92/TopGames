package com.sgsaez.topgames.domain.game

data class GamesException(var tag: Int, override var message: String = "") : Throwable(message) {
    companion object {
        const val DEFAULT = 1
        const val ERROR_NO_DATA_RECEIVED = 2
        const val ERROR_NO_DATA_FOUND = 3
        const val ERROR_INTERNET_CONNECTION = 4
    }
}