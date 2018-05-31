package com.sgsaez.topgames.utils

fun <T> condition(condition: () -> Boolean, yes: () -> T, no: () -> T) = if (condition()) yes() else no()
