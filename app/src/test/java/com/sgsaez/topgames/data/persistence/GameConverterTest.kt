package com.sgsaez.topgames.data.persistence

import com.sgsaez.topgames.data.persistence.typeconverters.GameConverter
import com.sgsaez.topgames.presentation.model.Image
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GameConverterTest {

    lateinit var gameConverter: GameConverter

    @Before
    fun setUp() {
        gameConverter = GameConverter()
    }

    @Test
    fun testFromImageUrlIsEmpty() {
        val result = gameConverter.fromImage(Image(""))
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun testFromImageUrlIsCorrect() {
        val result = gameConverter.fromImage(Image("url"))
        Assert.assertEquals("url", result)
    }
}