package com.ihegyi.alexiguitartuner


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.RECORD_AUDIO"
        )

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun navigationTest() {
        val listOfFragments = listOf<Pair<Int,Int>>(
            Pair(R.string.title_metronome,1),
            Pair(R.string.title_tuner,0),
            Pair(R.string.title_chords,2),
            Pair(R.string.title_tuner,0),
            Pair(R.string.title_sgc,3),
            Pair(R.string.title_tuner,0),
            Pair(R.string.title_settings,4),
            Pair(R.string.title_tuner,0),
            Pair(R.string.title_metronome,1),
            Pair(R.string.title_chords,2),
            Pair(R.string.title_metronome,1),
            Pair(R.string.title_sgc,3),
            Pair(R.string.title_metronome,1),
            Pair(R.string.title_settings,4),
            Pair(R.string.title_chords,2),
            Pair(R.string.title_sgc,3),
            Pair(R.string.title_chords,2),
            Pair(R.string.title_settings,4),
            Pair(R.string.title_sgc,3),
            Pair(R.string.title_settings,4),
            Pair(R.string.title_sgc,3),
        )
        var bottomNavigationItemView : ViewInteraction
        listOfFragments.forEach {
            bottomNavigationItemView = onView(
                allOf(
                    withContentDescription(it.first),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        ),
                        it.second
                    ),
                    isDisplayed()
                )
            )
            bottomNavigationItemView.perform(click())
        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int,
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
