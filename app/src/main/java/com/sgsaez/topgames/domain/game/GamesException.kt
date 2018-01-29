package com.sgsaez.topgames.domain.game

import com.sgsaez.topgames.domain.ErrorConstants

data class GamesException(var tag: ErrorConstants, override var message: String = "") : Throwable(message)