package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.network.ApiService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.data.persistence.daos.GameDao
import com.sgsaez.topgames.data.persistence.entities.Game
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.data.repositories.game.DefaultGameRepository
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.domain.game.GamesException
import com.sgsaez.topgames.support.domains.functional.Either
import com.sgsaez.topgames.support.domains.functional.fold
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

class GameRepositoryTest {

    @Mock
    lateinit var mockApiService: ApiService

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
        gameRepository = DefaultGameRepository(mockApiService, mockGameDao, mockConnectivityChecker)
    }

    @Test
    fun testGetFavouritesReceivedEmptyListAndEmitNoDataFound() {
        val gameList = GameList(emptyList())

        setUpMocks(gameList, true)
        val games = gameRepository.getGames("0", "")

        games shouldBeInstanceOf Either::class.java
        games.fold({ left ->
            left shouldBeInstanceOf GamesException::class.java
            left.error shouldBe GameError.ERROR_NO_DATA_RECEIVED
        }, {})
    }


    @Test
    fun testGetGamesIsOnlineReceivedListAndEmitList() {
        val gameList = GameList(listOf(Game("1", "This is the game", "My game", Image("url"))))

        setUpMocks(gameList, true)

        val games = gameRepository.getGames("0", "")

        games shouldBeInstanceOf Either::class.java
        games.fold({ fail() },
                { right ->
                    right.results[0] shouldBeInstanceOf Game::class.java
                    right.results[0].name `should contain` "My game"
                    right.results.size shouldBe 1
                })

        Mockito.verify(mockGameDao).insertAll(gameList.results)
    }

    private fun setUpMocks(modelFromUserService: GameList, isOnline: Boolean) {
        Mockito.`when`(mockConnectivityChecker.isOnline()).thenReturn(isOnline)
        Mockito.`when`(mockApiService.getGames("0")).thenReturn(mockGameCall)
        Mockito.`when`(mockGameCall.execute()).thenReturn(mockGameResponse)
        Mockito.`when`(mockGameResponse.body()).thenReturn(modelFromUserService)
        Mockito.`when`(mockGameDao.getGames()).thenReturn(emptyList())
    }

}