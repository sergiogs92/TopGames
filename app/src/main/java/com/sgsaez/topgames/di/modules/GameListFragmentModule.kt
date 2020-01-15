package com.sgsaez.topgames.di.modules

import androidx.lifecycle.ViewModelProvider
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.di.support.Factory
import com.sgsaez.topgames.domain.game.GetGames
import com.sgsaez.topgames.presentation.viewmodel.GameListViewModel
import dagger.Module
import dagger.Provides

@Module
class GameListFragmentModule {
    @Provides
    fun provideGetGames(gameRepository: GameRepository) = GetGames(gameRepository)

    @Provides
    fun providesViewModelFactory(getGames: GetGames): ViewModelProvider.Factory = Factory(GameListViewModel(getGames))

}