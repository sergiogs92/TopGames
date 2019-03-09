package com.sgsaez.topgames.support.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.sgsaez.topgames.R

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
    if (fragments == FIRST_FRAGMENT) finish() else supportFragmentManager.popBackStack()
}
