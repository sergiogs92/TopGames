package com.sgsaez.topgames.presentation

import com.sgsaez.topgames.domain.favourite.GetFavourites
import com.sgsaez.topgames.domain.favourite.RemoveFavourite
import com.sgsaez.topgames.domain.favourite.exception.FavouritesException
import com.sgsaez.topgames.presentation.model.GameViewModel
import com.sgsaez.topgames.presentation.presenters.FavouriteListPresenter
import com.sgsaez.topgames.presentation.view.FavouriteListView
import com.sgsaez.topgames.utils.SchedulerProviderTest
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class FavouriteListPresenterTest {

    @Mock
    lateinit var mockGetFavourites: GetFavourites

    @Mock
    lateinit var mockRemoveFavourite: RemoveFavourite

    @Mock
    lateinit var mockView: FavouriteListView

    lateinit var favouriteListPresenter: FavouriteListPresenter

    lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        val testSchedulerProvider = SchedulerProviderTest(testScheduler)
        favouriteListPresenter = FavouriteListPresenter(mockGetFavourites, mockRemoveFavourite, testSchedulerProvider)
    }

    @Test
    fun testGetFavouritesErrorCaseShowNoDataFound() {
        val single: Single<List<GameViewModel>> = Single.create { emitter ->
            emitter.onError(FavouritesException(FavouritesException.ERROR_NO_DATA_FOUND))
        }

        `when`(mockGetFavourites.execute()).thenReturn(single)
        favouriteListPresenter.attachView(mockView)
        favouriteListPresenter.onLoadFavourites()
        testScheduler.triggerActions()

        Mockito.verify(mockView).showNoDataFoundError()
    }

    @Test
    fun testGetFavouritesCorrectCaseShowList() {
        val favourites = listOf(GameViewModel("1", "This is the game", "My game", "url"))
        val single: Single<List<GameViewModel>> = Single.create { emitter ->
            emitter.onSuccess(favourites)
        }

        `when`(mockGetFavourites.execute()).thenReturn(single)
        favouriteListPresenter.attachView(mockView)
        favouriteListPresenter.onLoadFavourites()
        testScheduler.triggerActions()

        Mockito.verify(mockView).addFavouriteToList(favourites)
    }

    @Test
    fun testRemoveFavouriteCorrectCaseUpdateList() {
        val favourite = GameViewModel("1", "This is the game", "My game", "url")
        val single: Single<Unit> = Single.create { emitter ->
            emitter.onSuccess(Unit)
        }

        `when`(mockRemoveFavourite.execute(favourite)).thenReturn(single)
        favouriteListPresenter.attachView(mockView)
        favouriteListPresenter.onRemoveFavouriteGame(favourite)
        testScheduler.triggerActions()

        Mockito.verify(mockView).removeFavouriteToList(favourite)
    }
}