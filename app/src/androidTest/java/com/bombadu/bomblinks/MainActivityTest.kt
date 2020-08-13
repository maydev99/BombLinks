package com.bombadu.bomblinks

import android.app.Application
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.internal.inject.TargetContext
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    val LIST_ITEM_IN_TEST = 1

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


}