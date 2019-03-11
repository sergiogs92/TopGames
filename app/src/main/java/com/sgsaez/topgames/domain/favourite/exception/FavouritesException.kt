package com.sgsaez.topgames.domain.favourite.exception

sealed class FavoriteError {
    object FavouriteAlreadyExist : FavoriteError()
    object FavouritesNotFound : FavoriteError()
    object UnexpectedError : FavoriteError()
}