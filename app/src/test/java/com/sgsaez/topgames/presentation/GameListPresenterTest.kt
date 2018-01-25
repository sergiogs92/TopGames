package com.sgsaez.topgames.presentation

import com.sgsaez.topgames.domain.GetGames
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.model.Image
import com.sgsaez.topgames.presentation.presenters.GameListPresenter
import com.sgsaez.topgames.presentation.view.GameListView
import com.sgsaez.topgames.utils.SchedulerProviderTest
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
        val error = "Test error"
        val single: Single<List<Game>> = Single.create { emitter ->
            emitter.onError(Exception(error))
        }

        `when`(mockGetGames.execute("")).thenReturn(single)
        gameListPresenter.attachView(mockView)
        gameListPresenter.onLoadGames("")
        testScheduler.triggerActions()

        Mockito.verify(mockView).hideLoading()
    }

    @Test
    fun testGetGamesCorrectCaseShowGame() {
        val games = listOf(Game("1", "This is the game", "My game", Image("url")))
        val single: Single<List<Game>> = Single.create {
            emitter ->
            emitter.onSuccess(games)
        }

        `when`(mockGetGames.execute("")).thenReturn(single)
        gameListPresenter.attachView(mockView)
        gameListPresenter.onLoadGames("")
        testScheduler.triggerActions()

        Mockito.verify(mockView).addGameToList(games)
        Mockito.verify(mockView).hideLoading()
    }

}