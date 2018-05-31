package com.sgsaez.topgames.presentation.view.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.view.fragments.newGameListInstance
import com.sgsaez.topgames.utils.condition

private const val FIRST_FRAGMENT: Int = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listFragment = newGameListInstance("")
        (addFragment(listFragment))
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack("")
                .commit()
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.backStackEntryCount
        condition({ fragments == FIRST_FRAGMENT }, { finish() }, { super.onBackPressed() })
    }
}
