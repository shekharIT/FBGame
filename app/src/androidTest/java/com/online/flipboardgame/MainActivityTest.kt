package com.online.flipboardgame

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Rule
import org.junit.Test
import dagger.hilt.android.testing.HiltAndroidTest

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createEmptyComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun testMyAppLaunch() {
        ActivityScenario.launch(MainActivity::class.java)
        hiltRule.inject()
    }
}