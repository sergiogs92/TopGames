package com.sgsaez.topgames.domain.favourite.exception

class FavouritesException(val error: FavoriteError, override val message: String = "") : Throwable(message)

enum class FavoriteError {
    ERROR_NO_DATA_FOUND,
    FAVOURITE_ALREADY_EXITS
}