package com.sarangsho.plus

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun clearLocalLearningState() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("sarangsho_learning", android.content.Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }

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

        composeRule.onAllNodesWithText("বিশ্ব")[0].performClick()
        composeRule.onNodeWithText("জাতিসংঘ ও জলবায়ু আলোচনার প্রেক্ষাপট").assertIsDisplayed()
        composeRule.onNodeWithText("বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি")
            .assertDoesNotExist()
    }

    @Test
    fun libraryTabShowsOneClearEmptyStateAndSearchNoMatchState() {
        completeOnboarding()

        composeRule.onNodeWithText("লাইব্রেরি").performClick()
        composeRule.onNodeWithText("আপনার সংরক্ষিত বিষয়").assertIsDisplayed()
        composeRule.onNodeWithText("আপনার লাইব্রেরি এখনো খালি").assertIsDisplayed()
        composeRule.onNodeWithText("এখনো কোনো নিবন্ধ সংরক্ষণ করা হয়নি").assertDoesNotExist()
        composeRule.onNodeWithText("এখনো কোনো শব্দ বুকমার্ক করা হয়নি").assertDoesNotExist()

        composeRule.onNodeWithText("নিবন্ধ বা শব্দ লিখুন").performTextInput("no-such-saved-item")
        composeRule.onNodeWithText("কোনো সংরক্ষিত বিষয় মেলেনি").assertIsDisplayed()
        composeRule.onNodeWithText("আপনার লাইব্রেরি এখনো খালি").assertDoesNotExist()
    }
    @Test
    fun todayFeaturedArticleCanBeBookmarkedIntoLibrary() {
        completeOnboarding()

        composeRule.onNodeWithText("প্রতিবেদনটি পড়ুন").performClick()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .assertIsDisplayed()
        composeRule.onNodeWithContentDescription("সংরক্ষণ করুন").performClick()
        composeRule.onNodeWithContentDescription("সংরক্ষিত").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("লাইব্রেরি").performClick()
        composeRule.onNodeWithText("সংরক্ষিত নিবন্ধ • 1").assertIsDisplayed()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .assertIsDisplayed()
    }

    @Test
    fun learnVocabularyBookmarkAppearsInLibrary() {
        completeOnboarding()

        composeRule.onNodeWithText("শিখুন").performClick()
        composeRule.onNodeWithText("শব্দ শেখা শুরু করুন").performClick()
        composeRule.onNodeWithText("Resilience").assertIsDisplayed()
        composeRule.onAllNodesWithText("সংরক্ষণ")[0].performClick()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("লাইব্রেরি").performClick()
        composeRule.onNodeWithText("সংরক্ষিত শব্দ • 1").assertIsDisplayed()
        composeRule.onNodeWithText("Resilience").assertIsDisplayed()
    }

    @Test
    fun learningActivityShortcutsOpenTheirMatchingDestinations() {
        completeOnboarding()

        composeRule.onNodeWithText("শিখুন").performClick()
        composeRule.onNodeWithText("আপনার শেখার হিসাব • এই ডিভাইস").assertIsDisplayed()

        composeRule.onNodeWithText("সম্পন্ন পড়ার কাজ").assertHasClickAction().performClick()
        composeRule.onNodeWithText("আপনার পড়ার পরিকল্পনা").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("অনুশীলন সেশন").assertHasClickAction().performClick()
        composeRule.onNodeWithText("অনুশীলনের ইতিহাস").assertIsDisplayed()
        composeRule.onNodeWithText("এখনো কোনো সেশন সংরক্ষিত নেই").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("সংরক্ষিত শব্দ").assertHasClickAction().performClick()
        composeRule.onNodeWithText("আপনার সংরক্ষিত বিষয়").assertIsDisplayed()
    }

    @Test
    fun todayBriefingOpensTheEditionArchive() {
        completeOnboarding()

        composeRule.onNodeWithText("পুরোনো সংস্করণ").assertHasClickAction().performClick()
        composeRule.onNodeWithText("দৈনিক সংস্করণ").assertIsDisplayed()
        composeRule.onNodeWithText("গতকালের সংস্করণ").assertHasClickAction().performClick()
        composeRule.onNodeWithText("দৈনিক ব্রিফিং").assertIsDisplayed()
    }

    @Test
    fun settingsExposeLanguageSelectionAndPersistIt() {
        completeOnboarding()

        composeRule.onNodeWithContentDescription("মেনু").performClick()
        composeRule.onNodeWithText("অ্যাপের ভাষা").assertHasClickAction().performClick()
        composeRule.onNodeWithText("English").performClick()
        composeRule.onNodeWithText("নির্বাচন: English", substring = true).assertIsDisplayed()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("অ্যাপের ভাষা").performClick()
        composeRule.onNodeWithText("নির্বাচন: English", substring = true).assertIsDisplayed()
    }

    @Test
    fun vocabularyOpensTheSelectedWordDetail() {
        completeOnboarding()

        composeRule.onNodeWithText("শিখুন").performClick()
        composeRule.onNodeWithText("শব্দ শেখা শুরু করুন").performClick()
        composeRule.onAllNodesWithText("পূর্ণাঙ্গ পাঠ খুলুন")[0]
            .assertHasClickAction()
            .performClick()

        composeRule.onNodeWithText("Resilience").assertIsDisplayed()
        composeRule.onNodeWithText("Definition: The ability to recover or adapt after difficulty.")
            .assertIsDisplayed()
    }

    @Test
    fun learnHubPracticeFlowRecordsAndDisplaysACompletedAttempt() {
        completeOnboarding()

        composeRule.onNodeWithText("শিখুন").performClick()
        composeRule.onNodeWithText("আপনার শেখার হিসাব • এই ডিভাইস").assertIsDisplayed()
        composeRule.onNodeWithText("সংরক্ষিত শব্দ").assertIsDisplayed()
        composeRule.onNodeWithText("সম্পন্ন পড়ার কাজ").assertIsDisplayed()
        composeRule.onNodeWithText("অনুশীলন সেশন").assertIsDisplayed()

        composeRule.onNodeWithText("অনুশীলন সাজান").performClick()
        composeRule.onNodeWithText("নমুনা প্রশ্ন দেখুন").performClick()
        composeRule.onNodeWithText("বাংলাদেশের সংবিধান কবে গণপরিষদে গৃহীত হয়?")
            .assertIsDisplayed()
        composeRule.onNodeWithText("৪ নভেম্বর ১৯৭২").performClick()
        composeRule.onNodeWithText("উত্তর জমা দিন").performClick()
        composeRule.onNodeWithText("আপনার উত্তর সঠিক").assertIsDisplayed()
        composeRule.onNodeWithText("সেশন শেষ করুন").performClick()

        composeRule.onNodeWithText("অনুশীলন সম্পন্ন").assertIsDisplayed()
        composeRule.onNodeWithText("1 / 1").assertIsDisplayed()
        composeRule.onNodeWithText("অনুশীলনের ইতিহাস দেখুন").performClick()
        composeRule.onAllNodesWithText("অনুশীলনের ইতিহাস")[0].assertIsDisplayed()
        composeRule.onNodeWithText("1 / 1 সঠিক").assertIsDisplayed()
    }

}
