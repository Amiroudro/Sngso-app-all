package com.sarangsho.plus

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun welcomeScreenShowsProductNameAndStartAction() {
        composeRule.onNodeWithText("Sarangsho+").assertIsDisplayed()
        composeRule.onNodeWithText("শুরু করুন").assertIsDisplayed()
    }
}
