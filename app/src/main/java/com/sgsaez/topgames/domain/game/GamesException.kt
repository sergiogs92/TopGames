package com.sgsaez.topgames.domain.game

sealed class GameError {
    object GamesNotFound : GameError()
    object GamesNotReceived : GameError()
    object NetworkConnection : GameError()
    object UnexpectedError : GameError()
}