package com.sgsaez.topgames.di.components

import androidx.lifecycle.ViewModelProvider
import com.sgsaez.topgames.di.modules.GameDetailFragmentModule
import dagger.Subcomponent

@Subcomponent(modules = [GameDetailFragmentModule::class])
interface GameDetailFragmentComponent {
    fun factory(): ViewModelProvider.Factory
}
