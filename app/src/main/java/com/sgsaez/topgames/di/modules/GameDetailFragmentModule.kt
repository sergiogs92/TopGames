package com.sgsaez.topgames.di.modules

import androidx.lifecycle.ViewModelProvider
import com.sgsaez.topgames.di.support.Factory
import com.sgsaez.topgames.presentation.viewmodel.GameDetailViewModel
import dagger.Module
import dagger.Provides

@Module
class GameDetailFragmentModule {
    @Provides
    fun providesViewModelFactory(): ViewModelProvider.Factory = Factory(GameDetailViewModel())

}