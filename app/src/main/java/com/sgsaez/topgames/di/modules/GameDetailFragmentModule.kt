package com.sgsaez.topgames.di.modules

import android.content.Context
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import dagger.Module
import dagger.Provides

@Module
class GameDetailFragmentModule() {
    @Provides
    fun providePresenter(context: Context) = GameDetailPresenter(context)
}