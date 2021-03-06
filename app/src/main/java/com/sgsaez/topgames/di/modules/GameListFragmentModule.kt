package com.sgsaez.topgames.di.modules

import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.domain.game.GetGames
import com.sgsaez.topgames.presentation.presenters.GameListPresenter
import dagger.Module
import dagger.Provides

@Module
class GameListFragmentModule {
    @Provides
    fun provideGetGames(gameRepository: GameRepository) = GetGames(gameRepository)

    @Provides
    fun providePresenter(getGames: GetGames) = GameListPresenter(getGames)

}