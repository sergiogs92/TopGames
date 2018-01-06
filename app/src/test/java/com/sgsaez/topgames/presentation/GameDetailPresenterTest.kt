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
        gameDetailPresenter = GameDetailPresenter(context)
    }

    @Test
    fun testGetGameDetailCorrectCaseShowNoDescription() {
        val game = Game("1", null, "My game", Image("url"))
        val description = "No description"

        `when`(context.getString(anyInt())).thenReturn(description)
        gameDetailPresenter.attachView(mockView)
        gameDetailPresenter.paintDetail(game)

        Mockito.verify(mockView).addTitleToolbar(game.name)
        Mockito.verify(mockView).addDescription(description)
        Mockito.verify(mockView).addImage(game.image.url)
    }

    @Test
    fun testGetGameDetailCorrectCaseShowGame() {
        val game = Game("1", "This is the game", "My game", Image("url"))

        `when`(context.getString(anyInt())).thenReturn(game.description)
        gameDetailPresenter.attachView(mockView)
        gameDetailPresenter.paintDetail(game)

        Mockito.verify(mockView).addTitleToolbar(game.name)
        Mockito.verify(mockView).addDescription(game.description!!)
        Mockito.verify(mockView).addImage(game.image.url)
    }
}

