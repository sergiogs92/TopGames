package com.sgsaez.topgames.domain.game

class GamesException(val error: GameError, override val message: String = "") : Throwable(message)

enum class GameError {
    DEFAULT,
    ERROR_NO_DATA_RECEIVED,
    ERROR_NO_DATA_FOUND,
    ERROR_INTERNET_CONNECTION
}
