package com.sgsaez.topgames.presentation.view.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.view.fragments.GameListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listFragment = GameListFragment.newInstance("")
        (addFragment(listFragment))
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack("")
                .commit()
    }
}
