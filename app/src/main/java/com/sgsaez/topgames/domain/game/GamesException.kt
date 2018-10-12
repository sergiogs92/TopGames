package com.sgsaez.topgames.domain.game

data class GamesException(var error: GameError, override var message: String = "") : Throwable(message)

enum class GameError {
    DEFAULT,
    ERROR_NO_DATA_RECEIVED,
    ERROR_NO_DATA_FOUND,
    ERROR_INTERNET_CONNECTION
    }
