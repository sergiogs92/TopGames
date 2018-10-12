package com.sgsaez.topgames.utils

import android.os.Bundle
import android.support.v4.app.Fragment

fun <T> condition(condition: () -> Boolean, yes: () -> T, no: () -> T) = if (condition()) yes() else no()

fun <T : Fragment> withBundle(fragment: T, builder: Bundle.() -> Unit): T {
    fragment.arguments = Bundle().apply(builder)
    return fragment
}