package com.sgsaez.topgames.support

import android.os.Bundle
import android.support.v4.app.Fragment

fun <T : Fragment> withBundle(fragment: T, builder: Bundle.() -> Unit): T {
    fragment.arguments = Bundle().apply(builder)
    return fragment
}