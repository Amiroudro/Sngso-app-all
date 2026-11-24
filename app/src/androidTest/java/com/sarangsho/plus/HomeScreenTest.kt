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
        composeRule.onNodeWithText("প্রধান প্রতিবেদন").assertIsDisplayed()
    }

    @Test
    fun feedCategoryFilterShowsOnlyMatchingSampleStories() {
        composeRule.onNodeWithText("শুরু করুন").performClick()
        composeRule.onNodeWithText("এগিয়ে যান").performClick()
        composeRule.onNodeWithText("আমার ব্রিফিং দেখুন").performClick()

        composeRule.onNodeWithText("অনুসন্ধান").performClick()
        composeRule.onNodeWithText("বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি").assertIsDisplayed()

        composeRule.onNodeWithText("বিশ্ব").performClick()
        composeRule.onNodeWithText("জাতিসংঘ ও জলবায়ু আলোচনার সাম্প্রতিক অগ্রগতি").assertIsDisplayed()
        composeRule.onNodeWithText("বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি").assertDoesNotExist()
    }
}
