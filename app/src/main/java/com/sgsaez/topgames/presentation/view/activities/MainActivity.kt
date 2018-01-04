package com.sgsaez.topgames.presentation.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.view.fragments.GameListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addGameListFragment()
    }

    private fun addGameListFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, GameListFragment())
                .commit()
    }
}
