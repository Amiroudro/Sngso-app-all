package com.sarangsho.plus

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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

    @Test
    fun firstRunOnboardingLeadsToBriefing() {
        composeRule.onNodeWithText("শুরু করুন").performClick()
        composeRule.onNodeWithText("আপনি কী অর্জন করতে চান?").assertIsDisplayed()

        composeRule.onNodeWithText("এগিয়ে যান").performClick()
        composeRule.onNodeWithText("কোন বিষয়গুলো পড়তে চান?").assertIsDisplayed()

        composeRule.onNodeWithText("আমার ব্রিফিং দেখুন").performClick()
        composeRule.onNodeWithText("আজকের প্রধান বিষয়").assertIsDisplayed()
    }
}
