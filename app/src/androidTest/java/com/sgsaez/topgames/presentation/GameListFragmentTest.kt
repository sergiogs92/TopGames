package com.sgsaez.topgames.presentation

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sgsaez.topgames.R
import com.sgsaez.topgames.TopGamesApplication
import com.sgsaez.topgames.data.persistence.entities.EGame
import com.sgsaez.topgames.data.persistence.entities.GameList
import com.sgsaez.topgames.data.repositories.GameRepository
import com.sgsaez.topgames.di.components.DaggerTopGamesApplicationComponentMock
import com.sgsaez.topgames.di.modules.TopGamesApplicationModuleMock
import com.sgsaez.topgames.presentation.model.Image
import com.sgsaez.topgames.presentation.view.activities.MainActivity
import com.sgsaez.topgames.utils.RecyclerViewMatcher
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameListFragmentTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    lateinit var mockGameRepository: GameRepository

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    @Before
    fun setUp() {
        mockGameRepository = mock()
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
        onView(withId(R.id.toolbar)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.image)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.description)).check(ViewAssertions.matches(isDisplayed()))
    }

    private fun mockGames() {
        val mockSingle = Single.create<GameList> { emitter: SingleEmitter<GameList> ->
            val gameList = GameList(getMockGameList())
            emitter.onSuccess(gameList)
        }
        whenever(mockGameRepository.getGames()).thenReturn(mockSingle)
    }

    private fun getMockGameList(): List<EGame> = (1..10).map {
        val number = +it
        EGame(it.toString(), "This is the game $number", "Game $number", Image(""))
    }
}