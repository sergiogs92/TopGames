package com.sgsaez.topgames.di.components

import com.sgsaez.topgames.di.modules.GameListFragmentModule
import com.sgsaez.topgames.presentation.presenters.GameListPresenter
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(GameListFragmentModule::class))
interface GameListFragmentComponent {
    fun presenter() : GameListPresenter
}