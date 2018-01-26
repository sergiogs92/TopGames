package com.sgsaez.topgames.presentation

import android.text.Html
import android.text.SpannableStringBuilder
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.model.Image
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import com.sgsaez.topgames.presentation.view.GameDetailView
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

    lateinit var gameDetailPresenter: GameDetailPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.mockStatic(Html::class.java)
        gameDetailPresenter = GameDetailPresenter()
    }

    @Test
    fun testGetGameDetailCorrectCaseShowNoDescription() {
        val game = Game("1", "No description", "My game", Image("url"))

        (Mockito.`when`(Html.fromHtml(game.description))).thenReturn(SpannableStringBuilder("No description"))
        gameDetailPresenter.attachView(mockView)
        gameDetailPresenter.onInit(game)

        Mockito.verify(mockView).addTitleToolbar(game.name)
        Mockito.verify(mockView).addImage(game.image.url)
    }
}

