package com.sgsaez.topgames.presentation

import android.content.Context
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.model.Image
import com.sgsaez.topgames.presentation.presenters.GameDetailPresenter
import com.sgsaez.topgames.presentation.view.GameDetailView
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GameDetailPresenterTest {

    @Mock
    lateinit var mockView: GameDetailView

    @Mock
    lateinit var context: Context

    lateinit var gameDetailPresenter: GameDetailPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        gameDetailPresenter = GameDetailPresenter()
    }

    @Test
    fun testGetGameDetailCorrectCaseShowNoDescription() {
        val noDescription = "No description"
        val game = Game("1", null, "My game", Image("url"))

        `when`(context.getString(anyInt())).thenReturn(noDescription)
        gameDetailPresenter.attachView(mockView)
        gameDetailPresenter.onInit(noDescription, game)

        Mockito.verify(mockView).addTitleToolbar(game.name)
        Mockito.verify(mockView).addDescription(noDescription)
        Mockito.verify(mockView).addImage(game.image.url)
    }
}

