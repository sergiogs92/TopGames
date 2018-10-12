package com.sgsaez.topgames.data.repositories

import com.sgsaez.topgames.data.persistence.daos.FavouriteDao
import com.sgsaez.topgames.data.persistence.entities.Favourite
import com.sgsaez.topgames.data.persistence.entities.FavouriteList
import com.sgsaez.topgames.data.persistence.entities.Image
import com.sgsaez.topgames.data.repositories.favourite.DefaultFavouriteRepository
import com.sgsaez.topgames.data.repositories.favourite.FavouriteRepository
import com.sgsaez.topgames.domain.favourite.exception.FavoriteError
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

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
        val favourites = listOf(Favourite("1", "This is the game", "My game", Image("url")))

        setUpMocks(favourites)
        val testObserver = favouriteRepository.getFavorites().test()

        testObserver.assertNoErrors()
        testObserver.assertValue { favouritesResult: FavouriteList -> favouritesResult.results.size == 1 }
        Mockito.verify(mockFavouriteDao).getFavourites()
    }

    @Test
    fun testGetFavouritesReceivedEmptyListAndEmitNoDataFound() {
        val favourites = emptyList<Favourite>()

        setUpMocks(favourites)
        val testObserver = favouriteRepository.getFavorites().test()

        testObserver.assertError(FavouritesException(FavoriteError.ERROR_NO_DATA_FOUND))
    }

    @Test
    fun testAddFavouritesReceived() {
        val favourite = Favourite("1", "This is the game", "My game", Image("url"))

        val testObserver = favouriteRepository.addFavorite(favourite).test()

        testObserver.assertNoErrors()
        Mockito.verify(mockFavouriteDao).insertAll(favourite)
    }

    @Test
    fun testRemoveTargetFavourite() {
        val favourite = Favourite("1", "This is the game", "My game", Image("url"))

        val testObserver = favouriteRepository.removeFavorite(favourite).test()

        testObserver.assertNoErrors()
        Mockito.verify(mockFavouriteDao).deleteFavourite(favourite)
    }

    private fun setUpMocks(mockFavourites: List<Favourite>) {
        Mockito.`when`(mockFavouriteDao.getFavourites()).thenReturn(mockFavourites)
    }
}