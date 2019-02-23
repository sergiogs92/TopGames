package com.sgsaez.topgames.utils.navigation

import android.R
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.sgsaez.topgames.utils.condition

private const val FIRST_FRAGMENT: Int = 1

fun navigateTo(activity: FragmentActivity, fragment: Fragment) {
    activity.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .addToBackStack(null).replace(android.R.id.content, fragment)
            .commitAllowingStateLoss()
}

fun navigateBack(activity: FragmentActivity) {
    val fragments = activity.supportFragmentManager.backStackEntryCount
    condition({ fragments == FIRST_FRAGMENT }, { activity.finish() }, { activity.supportFragmentManager.popBackStack() })
}
