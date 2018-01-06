package com.sgsaez.topgames.domain

import com.sgsaez.topgames.data.persistence.entities.EGame
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.data.repositories.GameRepository
import com.sgsaez.topgames.presentation.model.Game
import com.sgsaez.topgames.presentation.model.Image
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetGamesTest {

    @Mock
    lateinit var mockGameRepository: GameRepository

    lateinit var getGames: GetGames

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        getGames = GetGames(mockGameRepository)
    }

    @Test
    fun testGameListWithOneItemEmitListWithOneGame() {
        val mockSingle = Single.create { e: SingleEmitter<GameList>? -> e?.onSuccess(GameList(listOf(EGame("1", "This is the game", "My game", Image("url"))))) }

        `when`(mockGameRepository.getGames()).thenReturn(mockSingle)

        val resultSingle = getGames.execute()
        val testObserver = resultSingle.test()
        testObserver.assertNoErrors()
        testObserver.assertValue { games: List<Game> -> games.size == 1 }
        testObserver.assertValue { games: List<Game> ->
            games[0] == Game("1", "This is the game", "My game", Image("url"))
        }
    }

    @Test
    fun testExecuteGameListEmptyEmitEmptyList() {
        val mockSingle = Single.create { e: SingleEmitter<GameList>? -> e?.onSuccess(GameList(emptyList())) }

        `when`(mockGameRepository.getGames()).thenReturn(mockSingle)

        val resultSingle = getGames.execute()
        val testObserver = resultSingle.test()
        testObserver.assertNoErrors()
        testObserver.assertValue { userViewModels: List<Game> -> userViewModels.isEmpty() }
    }
}