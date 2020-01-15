package com.sgsaez.topgames.di.components

import androidx.lifecycle.ViewModelProvider
import com.sgsaez.topgames.di.modules.GameListFragmentModule
import dagger.Subcomponent

@Subcomponent(modules = [GameListFragmentModule::class])
interface GameListFragmentComponent {
    fun factory(): ViewModelProvider.Factory
}

