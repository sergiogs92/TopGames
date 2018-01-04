package com.sgsaez.topgames.di.components

import com.sgsaez.topgames.di.modules.GameDetailFragmentModule
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(GameDetailFragmentModule::class))
interface GameDetailFragmentComponent {
    fun presenter() : GameDetailPresenter
}