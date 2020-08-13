package com.bombadu.bomblinks

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AddLinkActivityTest {

    @get: Rule
    val activityRule = ActivityScenarioRule(AddLinkActivity::class.java)


    @Test
    fun test_isActivityVisible() {
        onView(withId(R.id.add_link_activity))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_enterUrlIntoEditTextPressPreviewButton() {

        //Check if Objects are Visible
        onView(withId(R.id.url_edit_text))
            .check(matches(isDisplayed()))

        onView(withId(R.id.preview_button))
            .check(matches(isDisplayed()))

        //Checks the empty preview placeholder
        onView(withId(R.id.preview_placeholder_text_view))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        //Enter Url into TextView
        onView(withId(R.id.url_edit_text))
            .perform(typeText("http://bombadu.com"))

        //Click PreviewButton
        onView(withId(R.id.preview_button))
            .perform(click())

        //Checks the preview view is visible
        onView(withId(R.id.preview_view))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        //Enter Category
        onView(withId(R.id.categoryACTextView))
            .perform(typeText("Coding"))

        //Save Link to Database
        onView(withId(R.id.save_link)).perform(click())

    }

    @Test
    fun test_testBarCodeReader() {
        onView(withId(R.id.qr_reader))
            .perform(click())

        onView(withId(R.id.qr_reader_activity))
            .check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.add_link_activity))
            .check(matches(isDisplayed()))
    }
}