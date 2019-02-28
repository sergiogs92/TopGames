package com.sgsaez.topgames.support.domains.functional

sealed class Either<out L, out R> {

    data class Left<out L>(val value: L) : Either<L, Nothing>()

    data class Right<out R>(val value: R) : Either<Nothing, R>()

}

fun <L, R, T> Either<L, R>.fold(left: (L) -> T, right: (R) -> T): T =
        when (this) {
            is Either.Left -> left(value)
            is Either.Right -> right(value)
        }