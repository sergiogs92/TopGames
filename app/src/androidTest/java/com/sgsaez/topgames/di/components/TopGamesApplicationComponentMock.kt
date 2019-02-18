package com.sgsaez.topgames.di.components

import com.sgsaez.topgames.di.modules.TopGamesApplicationModuleMock
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TopGamesApplicationModuleMock::class])
interface TopGamesApplicationComponentMock : ApplicationComponent