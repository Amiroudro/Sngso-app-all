package com.sarangsho.plus

import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun completeOnboarding() {
        composeRule.onNodeWithText("আমার প্রস্তুতি সাজাই").performClick()
        composeRule.onNodeWithText("আপনার প্রধান লক্ষ্য কী?").assertIsDisplayed()
        composeRule.onNodeWithText("বিষয় বেছে নিন").performClick()
        composeRule.onNodeWithText("কোন বিষয়গুলো পড়তে চান?").assertIsDisplayed()
        composeRule.onNodeWithText("আমার ব্রিফিং দেখুন").performClick()
    }

    @Test
    fun welcomeScreenShowsProductNameAndStartAction() {
        composeRule.onNodeWithText("Sarangsho+").assertIsDisplayed()
        composeRule.onNodeWithText("আমার প্রস্তুতি সাজাই").assertIsDisplayed()
    }

    @Test
    fun firstRunOnboardingLeadsToTodayBriefing() {
        completeOnboarding()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .assertIsDisplayed()
    }

    @Test
    fun exploreTabAndCategoryFilterShowMatchingSampleStories() {
        completeOnboarding()

        composeRule.onNodeWithText("এক্সপ্লোর").performClick()
        composeRule.onNodeWithText("সকল সংবাদ").assertIsDisplayed()
        composeRule.onNodeWithText("বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি")
            .assertIsDisplayed()

        composeRule.onNodeWithText("বিশ্ব").performClick()
        composeRule.onNodeWithText("জাতিসংঘ ও জলবায়ু আলোচনার প্রেক্ষাপট").assertIsDisplayed()
        composeRule.onNodeWithText("বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি")
            .assertDoesNotExist()
    }

    @Test
    fun libraryTabOpensSavedItemsScreen() {
        completeOnboarding()

        composeRule.onNodeWithText("লাইব্রেরি").performClick()
        composeRule.onNodeWithText("আপনার সংরক্ষিত বিষয়").assertIsDisplayed()
        composeRule.onNodeWithText("সংরক্ষিত বিষয় খুঁজুন").assertIsDisplayed()
    }
}
