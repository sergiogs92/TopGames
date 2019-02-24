package com.sgsaez.topgames.presentation

import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.domain.game.GetGames
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.presenters.GameListPresenter
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.support.SchedulerProviderTest
import com.sgsaez.topgames.support.domains.Page
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GameListPresenterTest {

    @Mock
    lateinit var mockGetGames: GetGames

    @Mock
    lateinit var mockView: GameListView

    lateinit var gameListPresenter: GameListPresenter

    lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        val testSchedulerProvider = SchedulerProviderTest(testScheduler)
        gameListPresenter = GameListPresenter(mockGetGames, testSchedulerProvider)
    }

    @Test
    fun testGetGamesErrorCaseShowError() {
        val page: Page<GameViewModel> = Page()
        val single: Single<List<GameViewModel>> = Single.create { emitter ->
            emitter.onError(GamesException(GameError.DEFAULT))
        }

        `when`(mockGetGames.execute("0", "")).thenReturn(single)
        gameListPresenter.attachView(mockView)
        gameListPresenter.onLoadGames(page, false)
        testScheduler.triggerActions()

        Mockito.verify(mockView).hideLoading()
    }

    @Test
    fun testGetGamesCorrectCaseShowGame() {
        val games = listOf(GameViewModel("1", "This is the game", "My game", "url"))
        val single: Single<List<GameViewModel>> = Single.create { emitter ->
            emitter.onSuccess(games)
        }

        `when`(mockGetGames.execute("0", "")).thenReturn(single)
        gameListPresenter.attachView(mockView)
        gameListPresenter.onLoadGames(Page())
        testScheduler.triggerActions()

        Mockito.verify(mockView).addGameToList(false, false, games)
        Mockito.verify(mockView).hideLoading()
    }

}