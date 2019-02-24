package com.sgsaez.topgames.presentation.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sgsaez.topgames.R
import com.sgsaez.topgames.presentation.view.fragments.newGameListInstance
import com.sgsaez.topgames.support.navigation.navigateBack
import com.sgsaez.topgames.support.navigation.navigateTo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val newGameListFragment = newGameListInstance("")
        navigateTo(newGameListFragment)
    }

    override fun onBackPressed() = navigateBack()

}
