package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.network.ApiService
import com.sgsaez.topgames.data.network.connectivity.ConnectivityChecker
import com.sgsaez.topgames.data.repositories.game.*
import com.sgsaez.topgames.domain.game.GameError
import com.sgsaez.topgames.support.domains.functional.Either
import com.sgsaez.topgames.support.domains.functional.fold
import org.amshove.kluent.`should contain`
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response
import kotlin.test.fail

class GameRepositoryTest {

    @Mock
    lateinit var mockApiService: ApiService

    @Mock
    lateinit var mockConnectivityChecker: ConnectivityChecker

    @Mock
    lateinit var mockGameCall: Call<NetGameList>

    @Mock
    lateinit var mockGameResponse: Response<NetGameList>

    lateinit var gameRepository: GameRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gameRepository = DefaultGameRepository(mockApiService, mockConnectivityChecker)
    }

    @Test
    fun testGetFavouritesReceivedEmptyListAndEmitNoDataFound() {
        val gameList = NetGameList(emptyList())

        setUpMocks(gameList, true)
        val games = gameRepository.getGames("0", "")

        games shouldBeInstanceOf Either::class.java
        games.fold({ left ->
            left shouldBeInstanceOf GameError::class.java
            left shouldBe GameError.GamesNotReceived
        }, {})
    }

    @Test
    fun testGetGamesIsOnlineReceivedListAndEmitList() {
        val gameList = NetGameList(listOf(NetGame("1", "This is the game", "My game", NetImage("url"))))

        setUpMocks(gameList, true)

        val games = gameRepository.getGames("0", "")

        games shouldBeInstanceOf Either::class.java
        games.fold({ fail() },
                { right ->
                    right.results[0] shouldBeInstanceOf NetGame::class.java
                    right.results[0].name `should contain` "My game"
                    right.results.size shouldBe 1
                })

    }

    private fun setUpMocks(modelFromUserService: NetGameList, isOnline: Boolean) {
        Mockito.`when`(mockConnectivityChecker.isOnline()).thenReturn(isOnline)
        Mockito.`when`(mockApiService.getGames("0")).thenReturn(mockGameCall)
        Mockito.`when`(mockGameCall.execute()).thenReturn(mockGameResponse)
        Mockito.`when`(mockGameResponse.body()).thenReturn(modelFromUserService)
    }

}