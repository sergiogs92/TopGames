package com.sgsaez.topgames.presentation.viewmodel

import com.sgsaez.topgames.presentation.model.Game

class GameDetailViewModel : BaseViewModel<GameDetailState>(GameDetailState::class.java) {

    fun showGame(game: Game) = updateState(screenState.copy(game = game))

}

data class GameDetailState(val game: Game = Game())