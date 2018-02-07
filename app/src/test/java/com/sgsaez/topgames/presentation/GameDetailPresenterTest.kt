package com.sgsaez.topgames.presentation

import android.text.Html
import android.text.SpannableStringBuilder
import com.sgsaez.topgames.domain.favourite.AddFavourite
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import com.sgsaez.topgames.presentation.view.GameDetailView
import com.sgsaez.topgames.utils.SchedulerProviderTest
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Html::class)
class GameDetailPresenterTest {

    @Mock
    lateinit var mockView: GameDetailView

    @Mock
    lateinit var mockAddFavourite: AddFavourite

    lateinit var gameDetailPresenter: GameDetailPresenter

    lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.mockStatic(Html::class.java)
        testScheduler = TestScheduler()
        val testSchedulerProvider = SchedulerProviderTest(testScheduler)
        gameDetailPresenter = GameDetailPresenter(mockAddFavourite, testSchedulerProvider)
    }

    @Test
    fun testGetGameDetailCorrectCaseShowNoDescription() {
        val game = GameViewModel("1", "No description", "My game", "url")

        (Mockito.`when`(Html.fromHtml(game.description))).thenReturn(SpannableStringBuilder("No description"))
        gameDetailPresenter.attachView(mockView)
        gameDetailPresenter.onInit(game)

        Mockito.verify(mockView).addTitleToolbar(game.name)
        Mockito.verify(mockView).addImage(game.imageUrl)
    }

    @Test
    fun testGetGameDetailCorrectCaseShowSaveFavourite() {
        val game = GameViewModel("1", "Description", "My game", "url")
        val single: Single<Unit> = Single.create { emitter ->
            emitter.onSuccess(Unit)
        }

        Mockito.`when`(mockAddFavourite.execute(game)).thenReturn(single)
        (Mockito.`when`(Html.fromHtml(game.description))).thenReturn(SpannableStringBuilder("Description"))
        gameDetailPresenter.attachView(mockView)
        gameDetailPresenter.onInit(game)
        gameDetailPresenter.onSaveFavouriteGame(game)
        testScheduler.triggerActions()

        Mockito.verify(mockView).showSaveFavourite()
    }
}

