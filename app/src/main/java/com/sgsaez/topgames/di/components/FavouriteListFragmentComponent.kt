package com.sgsaez.topgames.di.components

import com.sgsaez.topgames.di.modules.FavouriteListFragmentModule
import com.sgsaez.topgames.presentation.presenters.FavouriteListPresenter
import dagger.Subcomponent

@Subcomponent(modules = [FavouriteListFragmentModule::class])
interface FavouriteListFragmentComponent {
    fun presenter(): FavouriteListPresenter
}