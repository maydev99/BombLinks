package com.bombadu.bomblinks

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.FixMethodOrder

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    val LIST_ITEM_IN_TEST = 0

    @Test
    fun test_isActivityInView() {
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isFabInView() {
        onView(withId(R.id.floatingActionButton)).check(matches(isDisplayed()))
    }

    @Test
    fun test_clickOnFabButtonToNavigateToAddScreen() {
        onView(withId(R.id.floatingActionButton)).check(matches(isDisplayed()))
        onView(withId(R.id.floatingActionButton)).perform(click())

        onView(withId(R.id.add_link_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun test_CategoryButtonClick() {
        onView(withId(R.id.category))
            .check(matches(isDisplayed()))


        onView(withId(R.id.category))
            .perform(click())

        pressBack()
    }

    @Test
    fun test_AboutButton() {
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withText("About"))
            .perform(click())

        pressBack()
    }

    @Test
    fun test_ItemClick() {
        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))

        onView(withId(R.id.recycler_view))
            .perform(actionOnItemAtPosition<MainAdapter.MainHolder>(LIST_ITEM_IN_TEST, click()))
    }


}