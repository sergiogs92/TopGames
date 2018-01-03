package com.sgsaez.topgames.presentation.presenters

import com.sgsaez.topgames.domain.GetGames
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.utils.SchedulerProvider

class GameListPresenter(private val getGames: GetGames,
                        private val schedulerProvider: SchedulerProvider) : BasePresenter<GameListView>() {
}