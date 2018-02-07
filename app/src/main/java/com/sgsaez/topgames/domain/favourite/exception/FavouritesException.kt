package com.sgsaez.topgames.domain.favourite.exception

data class FavouritesException(var tag: Int, override var message: String = "") : Throwable(message) {
    companion object {
        const val ERROR_NO_DATA_FOUND = 1
        const val FAVOURITE_ALREADY_EXITS = 2
    }
}