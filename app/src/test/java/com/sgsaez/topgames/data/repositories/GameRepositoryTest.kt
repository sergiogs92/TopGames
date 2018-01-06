package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.network.GameService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.data.persistence.daos.GameDao
import com.sgsaez.topgames.data.persistence.entities.EGame
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.presentation.model.Image
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

class GameRepositoryTest {

    @Mock
    lateinit var mockGameService: GameService

    @Mock
    lateinit var mockGameDao: GameDao

    @Mock
    lateinit var mockConnectivityChecker: ConnectivityChecker

    @Mock
    lateinit var mockGameCall: Call<GameList>

    @Mock
    lateinit var mockGameResponse: Response<GameList>

    lateinit var gameRepository: GameRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gameRepository = DefaultGameRepository(mockGameService, mockGameDao, mockConnectivityChecker)
    }

    @Test
    fun testGetGamesIsOnlineReceivedEmptyListAndEmitEmptyList() {
        val games = GameList(emptyList())

        setUpMocks(games, true)
        val testObserver = gameRepository.getGames().test()

        testObserver.assertNoErrors()
        testObserver.assertValue { gamesResult: GameList -> gamesResult.results.isEmpty() }
        Mockito.verify(mockGameDao).insertAll(games.results)
    }

    @Test
    fun testGetGamesIsOnlineReceivedListAndEmitList() {
        val games = GameList(listOf(EGame("1", "This is the game", "My game", Image("url"))))

        setUpMocks(games, true)
        val testObserver = gameRepository.getGames().test()

        testObserver.assertNoErrors()
        testObserver.assertValue { gamesResult: GameList -> gamesResult.results.size == 1 }
        Mockito.verify(mockGameDao).insertAll(games.results)
    }

    private fun setUpMocks(modelFromUserService: GameList, isOnline: Boolean) {
        Mockito.`when`(mockConnectivityChecker.isOnline()).thenReturn(isOnline)
        Mockito.`when`(mockGameService.getGames()).thenReturn(mockGameCall)
        Mockito.`when`(mockGameCall.execute()).thenReturn(mockGameResponse)
        Mockito.`when`(mockGameResponse.body()).thenReturn(modelFromUserService)
        Mockito.`when`(mockGameDao.getGames()).thenReturn(emptyList())
    }
}