package com.sgsaez.topgames.domain.favourite.exception

data class FavouritesException(var error: FavoriteError, override var message: String = "") : Throwable(message)

enum class FavoriteError {
    ERROR_NO_DATA_FOUND,
    FAVOURITE_ALREADY_EXITS
}