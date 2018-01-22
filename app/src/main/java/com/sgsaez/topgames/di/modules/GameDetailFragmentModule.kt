package com.sgsaez.topgames.di.modules

import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import dagger.Module
import dagger.Provides

@Module
class GameDetailFragmentModule {
    @Provides
    fun providePresenter() = GameDetailPresenter()
}