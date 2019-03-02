package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.persistence.daos.FavouriteDao
import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.data.repositories.favourite.DefaultFavouriteRepository
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
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
import kotlin.test.fail

class FavouriteRepositoryTest {

    @Mock
    lateinit var mockFavouriteDao: FavouriteDao

    lateinit var favouriteRepository: FavouriteRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        favouriteRepository = DefaultFavouriteRepository(mockFavouriteDao)
    }

    @Test
    fun testGetFavouritesReceivedListAndEmitList() {
        val favouriteList = listOf(Favourite("1", "This is the game", "My game", Image("url")))

        setUpMocks(favouriteList)
        val favourites = favouriteRepository.getFavorites()

        favourites shouldBeInstanceOf Either::class.java
        favourites.fold({ fail() },
                { right ->
                    right.results[0] shouldBeInstanceOf Favourite::class.java
                    right.results[0].name `should contain` "This is the game"
                    right.results.size shouldBe 1
                })

        Mockito.verify(mockFavouriteDao).getFavourites()
    }

    @Test
    fun testGetFavouritesReceivedEmptyListAndEmitNoDataFound() {
        val favouriteList = emptyList<Favourite>()

        setUpMocks(favouriteList)
        val favourites = favouriteRepository.getFavorites()

        favourites shouldBeInstanceOf Either::class.java
        favourites.fold({ left ->
            left shouldBeInstanceOf FavouritesException::class.java
            left.error shouldBe FavoriteError.ERROR_NO_DATA_FOUND
        }, {})
    }

    @Test
    fun testAddFavouritesReceived() {
        val favourite = Favourite("1", "This is the game", "My game", Image("url"))

        val favourites = favouriteRepository.addFavorite(favourite)

        favourites shouldBeInstanceOf Either::class.java
        favourites.fold({ fail() },
                { right ->
                    right shouldBeInstanceOf Unit::class.java
                })

        Mockito.verify(mockFavouriteDao).insertAll(favourite)
    }

    @Test
    fun testRemoveTargetFavourite() {
        val favourite = Favourite("1", "This is the game", "My game", Image("url"))

        val favourites = favouriteRepository.removeFavorite(favourite)

        favourites shouldBeInstanceOf Either::class.java
        favourites.fold({ fail() },
                { right ->
                    right shouldBeInstanceOf Unit::class.java
                })

        Mockito.verify(mockFavouriteDao).deleteFavourite(favourite)

    }

    private fun setUpMocks(mockFavourites: List<Favourite>) {
        Mockito.`when`(mockFavouriteDao.getFavourites()).thenReturn(mockFavourites)
    }

}