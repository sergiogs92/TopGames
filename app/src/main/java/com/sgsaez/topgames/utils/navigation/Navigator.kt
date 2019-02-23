package com.sgsaez.topgames.utils.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.sgsaez.topgames.R
import com.sgsaez.topgames.utils.condition

private const val FIRST_FRAGMENT: Int = 1

fun FragmentActivity.navigateTo(fragment: Fragment) {
    supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commitAllowingStateLoss()
}

fun FragmentActivity.navigateBack() {
    val fragments = supportFragmentManager.backStackEntryCount
    condition({ fragments == FIRST_FRAGMENT }, { finish() }, { supportFragmentManager.popBackStack() })
}
