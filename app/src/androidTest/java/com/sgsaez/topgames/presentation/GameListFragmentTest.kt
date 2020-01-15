package com.sgsaez.topgames.presentation

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sgsaez.topgames.R
import com.sgsaez.topgames.TopGamesApplication
import com.sgsaez.topgames.data.repositories.game.GameRepository
import com.sgsaez.topgames.data.repositories.game.NetGame
import com.sgsaez.topgames.data.repositories.game.NetGameList
import com.sgsaez.topgames.data.repositories.game.NetImage
import com.sgsaez.topgames.di.modules.TopGamesApplicationModuleMock
import com.sgsaez.topgames.presentation.view.activities.MainActivity
import com.sgsaez.topgames.support.RecyclerViewMatcher
import com.sgsaez.topgames.support.domains.functional.Either
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class GameListFragmentTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    lateinit var mockGameRepository: GameRepository

    lateinit var mockFavouriteRepository: FavouriteRepository

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    @Before
    fun setUp() {
        mockGameRepository = mock()
        mockFavouriteRepository = mock()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as TopGamesApplication
        val testComponent = DaggerTopGamesApplicationComponentMock.builder()
                .topGamesApplicationModuleMock(TopGamesApplicationModuleMock(app.applicationContext))
                .build()
        app.component = testComponent
    }

    @Test
    fun testShowingCorrectItems() {
        mockGames()
        activityRule.launchActivity(Intent())
        checkNameOnPosition(0, "Game 1")
        checkNameOnPosition(5, "Game 6")
    }

    private fun checkNameOnPosition(position: Int, expectedName: String) {
        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(position, R.id.name))
                .check(matches(withText(expectedName)))
    }

    @Test
    fun testOpenGameDetailOnItemClick() {
        mockGames()
        activityRule.launchActivity(Intent())
        checkClickOnPosition(0)
        checkElementsDisplayInDetail()
    }

    private fun checkClickOnPosition(position: Int) {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
    }

    private fun checkElementsDisplayInDetail() {
        onView(withId(R.id.detailToolbar)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.image)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.description)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun testSearchOneItem() {
        mockGames()
        activityRule.launchActivity(Intent())
        checkClickInSearchView()
        checkSearch()
    }

    private fun checkClickInSearchView() {
        onView(withId(R.id.game_list_searchView)).perform(click())
    }

    private fun checkSearch() {
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("Game 1"), pressImeActionButton())
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.listToolbar)))).check(matches(withText("Search: Game 1")))
    }

    @Test
    fun testAddFavouriteOneItem() {
        mockGames()
        activityRule.launchActivity(Intent())
        checkClickOnPosition(0)
        checkClickInFavourite()
        checkSavedFavourite()
    }

    private fun checkClickInFavourite() {
        onView(withId(R.id.game_detail_favourite)).perform(click())
    }

    private fun checkSavedFavourite() {
        onView(withText(R.string.saved_game)).inRoot(withDecorView(not(activityRule.activity.window.decorView))).check(matches(isDisplayed()))
    }

    private fun mockGames() {
        val gameList = getMockGameList()
        val mockSingle = Either.Right(gameList)

        whenever(mockGameRepository.getGames("0", "")).thenReturn(mockSingle)
    }

    private fun getMockGameList(): NetGameList = NetGameList((1..10).map {
        val number = +it
        val url = "goo.gl/svPzkf"
        NetGame(it.toString(), "This is the game $number", "Game $number", NetImage(url))
    })
}
