package com.sarangsho.plus

import com.sarangsho.plus.data.LocalLearningStore
import com.sarangsho.plus.data.ReaderPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import java.time.Instant
import org.junit.Assert.assertEquals
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun clearLocalLearningState() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("sarangsho_learning", android.content.Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
        val editionFixture = """{
          "edition_date":"2026-11-27","edition_day":"Friday","coverage":"Test coverage window","status":"published",
          "stories":[
            {"id":"CA01","category":"Bangladesh","headline":"Building a climate-resilient Bangladesh","summary":"English summary","one_liner":"English one liner","why_it_matters":"Why it matters","exam_facts":"Exam facts","detail":"Detailed reading","key_facts":[],"sources":[{"publication":"Test Publisher","headline":"Original report","date":"2026-11-27","url":"https://example.org/story-1"}]},
            {"id":"CA02","category":"Economy & Banking","headline":"বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি","summary":"Economy summary","one_liner":"Economy one liner","why_it_matters":"Economy context","exam_facts":"Economy facts","detail":"Economy detail","key_facts":[],"sources":[]},
            {"id":"CA03","category":"World","headline":"জাতিসংঘ ও জলবায়ু আলোচনার প্রেক্ষাপট","summary":"World summary","one_liner":"World one liner","why_it_matters":"World context","exam_facts":"World facts","detail":"World detail","key_facts":[],"sources":[]},
            {"id":"CA04","category":"Editorial & Opinion","headline":"গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন","summary":"Gambia summary","one_liner":"Gambia one liner","why_it_matters":"Gambia context","exam_facts":"Gambia facts","detail":"Gambia detail","key_facts":[],"sources":[]}
          ]
        }"""
        val bengaliFixture = """{
          "edition_date":"2026-11-27","status":"published","language":"bn",
          "stories":{
            "CA01":{"headline":"গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন","summary":"আন্তর্জাতিক বিচারিক প্রক্রিয়ার বাংলা সারাংশ।","one_liner":"এক বাক্যে মূল বিষয়।","why_it_matters":"পরীক্ষার জন্য গুরুত্বপূর্ণ প্রেক্ষাপট।","exam_facts":"আদালত ও আন্তর্জাতিক আইন সম্পর্কিত তথ্য।","detail":"বিস্তারিত বাংলা পাঠ।","key_facts":[]},
            "CA02":{"headline":"বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি","summary":"অর্থনীতির সারাংশ।"},
            "CA03":{"headline":"জাতিসংঘ ও জলবায়ু আলোচনার প্রেক্ষাপট","summary":"বিশ্বের সারাংশ।"},
            "CA04":{"headline":"রাষ্ট্রীয় প্রতিষ্ঠান ও সংবিধান","summary":"রাষ্ট্রীয় প্রতিষ্ঠানের সারাংশ।"}
          }
        }"""
        context.getSharedPreferences("sarangsho_edition_feed", android.content.Context.MODE_PRIVATE)
            .edit()
            .clear()
            .putString("edition_data", editionFixture)
            .putString("edition_bn", bengaliFixture)
            .putString("fetched_at", Instant.now().toString())
            .commit()
        runBlocking {
            ReaderPreferencesRepository.setFontSize(context, 18f)
            ReaderPreferencesRepository.setLineSpacing(context, 1.5f)
            ReaderPreferencesRepository.setSepia(context, false)
        }
    }

    @After
    fun clearLearningPreferencesAfterTest() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("sarangsho_learning", android.content.Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }

    private fun completeOnboarding() {
        composeRule.onNodeWithText("আমার প্রস্তুতি সাজাই").performClick()
        composeRule.onNodeWithText("আপনার প্রধান লক্ষ্য কী?").assertIsDisplayed()
        composeRule.onNodeWithText("বিষয় বেছে নিন").performScrollTo().performClick()
        composeRule.onNodeWithText("কোন বিষয়গুলো পড়তে চান?").assertIsDisplayed()
        composeRule.onNodeWithText("আমার ব্রিফিং দেখুন").performScrollTo().performClick()
        composeRule.onNodeWithText("এক্সপ্লোর").assertIsDisplayed()
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
            .performScrollTo().assertIsDisplayed()
    }

    @Test
    fun returningUserLaunchesDirectlyIntoTodayBriefing() {
        completeOnboarding()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals(true, LocalLearningStore.hasCompletedOnboarding(context))

        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Sarangsho+").assertIsDisplayed()
        composeRule.onNodeWithText("আমার প্রস্তুতি সাজাই").assertDoesNotExist()
    }

    @Test
    fun revisionDeckDisclaimsUnmeasuredProgress() {
        completeOnboarding()

        composeRule.onNodeWithText("শিখুন").performClick()
        composeRule.onNodeWithText("রিভিশন শুরু করুন").performScrollTo().performClick()

        composeRule.onNodeWithText("নমুনা কার্ড").assertIsDisplayed()
        composeRule.onNodeWithText("এই ডেমো সেশনে ব্যক্তিগত অগ্রগতি গণনা বা সংরক্ষণ করা হচ্ছে না।")
            .assertIsDisplayed()
        composeRule.onNodeWithText(
            "সম্পূর্ণ রিভিশন সেশন, কার্ডের সংখ্যা ও শেখার অগ্রগতি ব্যক্তিগত স্টাডি ডেটা সংযুক্ত হলে দেখানো হবে।"
        ).assertIsDisplayed()
        composeRule.onNodeWithText("কার্ড ৭ / ২০ • নমুনা সেশন").assertDoesNotExist()
    }

    @Test
    fun reportStoryDraftCanBeSavedRestoredAndClearedFromTheArticleScreen() {
        completeOnboarding()

        composeRule.onNodeWithText("প্রতিবেদনটি পড়ুন").performScrollTo().performClick()
        composeRule.onNodeWithText("তথ্য সংশোধনের অনুরোধ").performScrollTo().performClick()
        composeRule.onNodeWithTag("report-story-details").assertIsDisplayed()

        val draft = "প্রকাশের তারিখটি মূল উৎসের সঙ্গে মিলিয়ে দেখুন।"
        composeRule.onNodeWithTag("report-story-details").performTextInput(draft)
        composeRule.onNodeWithTag("report-story-save").performClick()
        composeRule.onNodeWithText("খসড়া সংরক্ষিত হয়েছে। এটি সার্ভারে পাঠানো হয়নি।").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("প্রতিবেদনটি পড়ুন").performScrollTo().performClick()
        composeRule.onNodeWithText("তথ্য সংশোধনের অনুরোধ").performScrollTo().performClick()
        val restored = composeRule.onNodeWithTag("report-story-details")
            .fetchSemanticsNode().config[SemanticsProperties.EditableText].text
        assertEquals(draft, restored)

        composeRule.onNodeWithTag("report-story-clear").performClick()
        val cleared = composeRule.onNodeWithTag("report-story-details")
            .fetchSemanticsNode().config[SemanticsProperties.EditableText].text
        assertEquals("", cleared)
    }

    @Test
    fun exploreTabAndCategoryFilterShowPublishedStories() {
        completeOnboarding()

        composeRule.onNodeWithText("এক্সপ্লোর").performClick()
        composeRule.onNodeWithText("সকল সংবাদ").assertIsDisplayed()
        composeRule.onNodeWithText("বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি")
            .performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag("feed-filter-world").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("feed-active-filter").assertTextEquals("ফিল্টার: বিশ্ব")
        composeRule.onNodeWithText("জাতিসংঘ ও জলবায়ু আলোচনার প্রেক্ষাপট").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি")
            .assertDoesNotExist()

        composeRule.onNodeWithTag("feed-filter-all").performScrollTo().performClick()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("বিস্তারিত পড়ুন").performScrollTo().performClick()
        composeRule.onNodeWithText("উৎস যাচাইয়ের অবস্থা").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithContentDescription("পেছনে").performClick()

        composeRule.onNodeWithTag("feed-filter-economy").performScrollTo().performClick()
        composeRule.onNodeWithText("বাংলাদেশের অর্থনীতি: নীতি, মূল্যস্ফীতি ও প্রবৃদ্ধি")
            .performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("বিস্তারিত পড়ুন").performScrollTo().performClick()
        composeRule.onNodeWithText("অর্থনীতির সারাংশ।").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun libraryTabShowsOneClearEmptyStateAndSearchNoMatchState() {
        completeOnboarding()

        composeRule.onNodeWithText("লাইব্রেরি").performClick()
        composeRule.onNodeWithText("আপনার সংরক্ষিত বিষয়").assertIsDisplayed()
        composeRule.onNodeWithText("আপনার লাইব্রেরি এখনো খালি").assertIsDisplayed()
        composeRule.onNodeWithText("এখনো কোনো নিবন্ধ সংরক্ষণ করা হয়নি").assertDoesNotExist()
        composeRule.onNodeWithText("এখনো কোনো শব্দ বুকমার্ক করা হয়নি").assertDoesNotExist()

        composeRule.onNodeWithTag("saved-search-field").performTextInput("no-such-saved-item")
        composeRule.onNodeWithText("কোনো সংরক্ষিত বিষয় মেলেনি").assertIsDisplayed()
        composeRule.onNodeWithText("আপনার লাইব্রেরি এখনো খালি").assertDoesNotExist()
    }
    @Test
    fun todayFeaturedArticleCanBeBookmarkedIntoLibrary() {
        completeOnboarding()

        composeRule.onNodeWithText("প্রতিবেদনটি পড়ুন").performScrollTo().performClick()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithContentDescription("সংরক্ষণ করুন").performClick()
        composeRule.onNodeWithContentDescription("সংরক্ষিত").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("লাইব্রেরি").performClick()
        composeRule.onNodeWithText("সংরক্ষিত নিবন্ধ • 1").assertIsDisplayed()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .performScrollTo().assertIsDisplayed()
    }

    @Test
    fun unknownSavedArticleIdsNeverOpenTheSampleArticle() {
        completeOnboarding()
        InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("sarangsho_learning", android.content.Context.MODE_PRIVATE)
            .edit()
            .putStringSet("saved_articles", setOf("legacy-story-123"))
            .commit()

        composeRule.onNodeWithText("লাইব্রেরি").performClick()
        composeRule.onNodeWithText("legacy-story-123").assertIsDisplayed()
        composeRule.onNodeWithText("এই নিবন্ধের বিস্তারিত এখনো উপলভ্য নয়।").assertIsDisplayed()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .assertDoesNotExist()

        composeRule.onNodeWithText("সংরক্ষিত সংগ্রহ").performClick()
        composeRule.onNodeWithText("legacy-story-123").assertIsDisplayed()
        composeRule.onNodeWithText("এই নিবন্ধের বিস্তারিত এখনো উপলভ্য নয়।").assertIsDisplayed()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .assertDoesNotExist()
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
    fun savedCollectionsOpenSavedArticlesAndBookmarkedWords() {
        completeOnboarding()

        composeRule.onNodeWithText("প্রতিবেদনটি পড়ুন").performScrollTo().performClick()
        composeRule.onNodeWithContentDescription("সংরক্ষণ করুন").performClick()
        composeRule.onNodeWithContentDescription("পেছনে").performClick()

        composeRule.onNodeWithText("শিখুন").performClick()
        composeRule.onNodeWithText("শব্দ শেখা শুরু করুন").performClick()
        composeRule.onNodeWithText("Resilience").assertIsDisplayed()
        composeRule.onNodeWithTag("word-bookmark-Resilience").performClick()
        val savedWords = InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("sarangsho_learning", android.content.Context.MODE_PRIVATE)
            .getStringSet("bookmarked_words", emptySet())
        assertEquals(setOf("Resilience"), savedWords)
        composeRule.onNodeWithContentDescription("পেছনে").performClick()

        composeRule.onNodeWithText("লাইব্রেরি").performClick()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Resilience").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithText("পড়ুন").performClick()
        composeRule.onNodeWithText("উৎস যাচাইয়ের অবস্থা").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithContentDescription("পেছনে").performClick()

        composeRule.onNodeWithText("খুলুন").performScrollTo().performClick()
        composeRule.onNodeWithText("Definition: The ability to recover or adapt after difficulty.")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Resilience").assertIsDisplayed()
        composeRule.onNodeWithText("সংরক্ষিত").performClick()
        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("Resilience").assertDoesNotExist()

        composeRule.onNodeWithText("Vocabulary Lab").performClick()
        composeRule.onNodeWithText("শব্দ, অর্থ, সমার্থক বা উদাহরণ খুঁজুন")
            .performTextInput("Mitigate")
        composeRule.onNodeWithTag("vocabulary-result-Mitigate").assertIsDisplayed()
        composeRule.onNodeWithText("সংরক্ষণ").performClick()
        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("Mitigate").assertIsDisplayed()

        composeRule.onNodeWithText("সংরক্ষণ সরান").performClick()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .assertDoesNotExist()
        composeRule.onAllNodesWithText("সরান")[0].performClick()
        composeRule.onNodeWithText("Mitigate").assertDoesNotExist()
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
        composeRule.onAllNodesWithText("অনুশীলনের ইতিহাস")[0].assertIsDisplayed()
        composeRule.onNodeWithText("এখনো কোনো সেশন সংরক্ষিত নেই").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("সংরক্ষিত শব্দ").assertHasClickAction().performClick()
        composeRule.onNodeWithText("আপনার সংরক্ষিত বিষয়").assertIsDisplayed()
    }

    @Test
    fun todayBriefingOpensTheEditionArchive() {
        completeOnboarding()

        composeRule.onNodeWithText("পুরোনো সংস্করণ").performScrollTo().assertHasClickAction().performClick()
        composeRule.onNodeWithText("দৈনিক সংস্করণ").assertIsDisplayed()
        composeRule.onNodeWithText("গতকালের সংস্করণ").assertHasClickAction().performClick()
        composeRule.onNodeWithText("দৈনিক ব্রিফিং").assertIsDisplayed()
        composeRule.onNodeWithText("প্রকাশিত সংস্করণ লোড হলে এখানে তার যাচাইকৃত প্রতিবেদন দেখানো হবে।")
            .performScrollTo().assertIsDisplayed()
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
    fun readerFontPreferenceCanBeChangedAndPersistsWhenReturningToArticle() {
        completeOnboarding()

        composeRule.onNodeWithText("প্রতিবেদনটি পড়ুন").performScrollTo().performClick()
        composeRule.onNodeWithText("পড়ার পছন্দ").performScrollTo().performClick()
        var fontSizeAccepted = false
        composeRule.onNodeWithTag("reader-font-size-slider")
            .performSemanticsAction(SemanticsActions.SetProgress) { setProgress ->
                fontSizeAccepted = setProgress(22f)
            }
        org.junit.Assert.assertTrue(
            "Font-size slider must accept the requested progress value",
            fontSizeAccepted
        )

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeRule.waitForIdle()
        val settings = runBlocking {
            kotlinx.coroutines.withTimeout(10_000) {
                ReaderPreferencesRepository.observe(context).first { it.fontSize == 22f }
            }
        }
        org.junit.Assert.assertEquals(22f, settings.fontSize, 0.1f)

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("পড়ার পছন্দ").performScrollTo().performClick()
        composeRule.onNodeWithText("লেখার আকার: 22 sp").assertIsDisplayed()
    }

    @Test
    fun readerLineSpacingAndSepiaPreferencesPersistWhenReturningToArticle() {
        completeOnboarding()

        composeRule.onNodeWithText("প্রতিবেদনটি পড়ুন").performScrollTo().performClick()
        composeRule.onNodeWithText("পড়ার পছন্দ").performScrollTo().performClick()

        var lineSpacingAccepted = false
        composeRule.onNodeWithTag("reader-line-spacing-slider")
            .performSemanticsAction(SemanticsActions.SetProgress) { setProgress ->
                lineSpacingAccepted = setProgress(1.8f)
            }
        org.junit.Assert.assertTrue(
            "Line-spacing slider must accept the requested progress value",
            lineSpacingAccepted
        )
        composeRule.onNodeWithTag("reader-sepia-switch").performClick()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeRule.waitForIdle()
        val settings = runBlocking {
            kotlinx.coroutines.withTimeout(10_000) {
                ReaderPreferencesRepository.observe(context).first { it.lineSpacing == 1.8f && it.sepia }
            }
        }
        org.junit.Assert.assertEquals(1.8f, settings.lineSpacing, 0.02f)
        org.junit.Assert.assertTrue(settings.sepia)

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("গাম্বিয়া বনাম মিয়ানমার: আন্তর্জাতিক বিচারিক প্রক্রিয়া বুঝে নিন")
            .performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("পড়ার পছন্দ").performScrollTo().performClick()
        composeRule.onNodeWithText("লাইন স্পেসিং: 1.8").assertIsDisplayed()
        composeRule.onNodeWithTag("reader-sepia-switch").assertIsOn()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("তিন-স্তরের সারাংশ").performScrollTo().performClick()
        composeRule.onNodeWithText("একই ঘটনা, তিনভাবে বুঝুন").assertIsDisplayed()
        composeRule.onNodeWithText("ঘটনাটির মূল বিষয় এক বাক্যে বুঝুন।").performScrollTo().assertIsDisplayed()
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
    fun vocabularySearchMatchesSynonymsExamplesAndTrimsWhitespace() {
        completeOnboarding()

        composeRule.onNodeWithText("শিখুন").performClick()
        composeRule.onNodeWithText("শব্দ শেখা শুরু করুন").performClick()
        val search = composeRule.onNodeWithText("শব্দ, অর্থ, সমার্থক বা উদাহরণ খুঁজুন")
        search.performTextInput(" adaptability ")

        composeRule.onNodeWithText("Resilience").assertIsDisplayed()
        composeRule.onNodeWithText("Mitigate").assertDoesNotExist()

        search.performTextClearance()
        search.performTextInput("natural disaster")
        composeRule.onNodeWithText("Resilience").assertIsDisplayed()
        composeRule.onNodeWithText("Mitigate").assertDoesNotExist()
    }

    @Test
    fun selectedWordCanBeBookmarkedFromItsDetailScreen() {
        completeOnboarding()

        composeRule.onNodeWithText("শিখুন").performClick()
        composeRule.onNodeWithText("শব্দ শেখা শুরু করুন").performClick()
        composeRule.onAllNodesWithText("পূর্ণাঙ্গ পাঠ খুলুন")[0].performClick()
        composeRule.onNodeWithText("Resilience").assertIsDisplayed()

        composeRule.onNodeWithText("সংরক্ষণ").performClick()
        composeRule.onNodeWithText("সংরক্ষিত").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        // Returning from Word Detail must refresh the Vocabulary list from local persistence.
        composeRule.onNodeWithText("সংরক্ষিত").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("লাইব্রেরি").performClick()
        composeRule.onNodeWithText("সংরক্ষিত শব্দ • 1").assertIsDisplayed()
        composeRule.onNodeWithText("Resilience").assertIsDisplayed()

        composeRule.onNodeWithText("খুলুন").performClick()
        composeRule.onNodeWithText("Definition: The ability to recover or adapt after difficulty.")
            .assertIsDisplayed()
        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("সংরক্ষিত শব্দ • 1").assertIsDisplayed()
    }

    @Test
    fun profileScreenDoesNotOfferNavigationToItself() {
        completeOnboarding()

        composeRule.onNodeWithContentDescription("প্রোফাইল").performClick()
        composeRule.onAllNodesWithText("আমার প্রোফাইল")[0].assertIsDisplayed()
        composeRule.onNodeWithContentDescription("প্রোফাইল").assertDoesNotExist()

        composeRule.onNodeWithContentDescription("পেছনে").performClick()
        composeRule.onNodeWithText("Sarangsho+").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("প্রোফাইল").assertIsDisplayed()
    }

    @Test
    fun learnHubPracticeFlowRecordsAndDisplaysACompletedAttempt() {
        completeOnboarding()

        composeRule.onNodeWithText("শিখুন").performClick()
        composeRule.onNodeWithText("আপনার শেখার হিসাব • এই ডিভাইস").assertIsDisplayed()
        composeRule.onNodeWithText("সংরক্ষিত শব্দ").assertIsDisplayed()
        composeRule.onNodeWithText("সম্পন্ন পড়ার কাজ").assertIsDisplayed()
        composeRule.onNodeWithText("অনুশীলন সেশন").assertIsDisplayed()

        composeRule.onNodeWithTag("learn-hub-practice-setup").performScrollTo().performClick()
        composeRule.onNodeWithTag("quiz-setup-start-practice").performScrollTo().performClick()
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
