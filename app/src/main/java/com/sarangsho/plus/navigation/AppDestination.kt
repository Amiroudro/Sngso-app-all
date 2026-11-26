package com.sarangsho.plus.navigation

import java.net.URI

/**
 * Stable, typed identifiers for the app's current destinations.
 * Keep route strings at this boundary while feature call sites migrate.
 */
enum class AppDestination(val route: String) {
    Welcome("welcome"),
    Signin("signin"),
    Home("home"),
    Learn("learn"),
    Feed("feed"),
    Article("article"),
    Summary("summary"),
    Timeline("timeline"),
    Perspectives("perspectives"),
    Bangladesh("bangladesh"),
    World("world"),
    Graph("graph"),
    Data("data"),
    Vocabulary("vocabulary"),
    Challenge("challenge"),
    Answer("answer"),
    Writing("writing"),
    Revision("revision"),
    Search("search"),
    Saved("saved"),
    Profile("profile"),
    OnboardingGoals("onboarding-goals"),
    OnboardingInterests("onboarding-interests"),
    Archive("archive"),
    Edition("edition"),
    TopicDetail("topic-detail"),
    Sources("sources"),
    QuizSetup("quiz-setup"),
    QuizResults("quiz-results"),
    WordDetail("word-detail"),
    StudyPlan("study-plan"),
    Notifications("notifications"),
    Settings("settings"),
    ReadingSettings("reading-settings"),
    Downloads("downloads"),
    Help("help"),
    About("about"),
    Feedback("feedback"),
    Privacy("privacy"),
    Language("language"),
    PracticeHistory("practice-history"),
    ReadingHistory("reading-history"),
    SavedCollections("saved-collections"),
    SourceDetail("source-detail"),
    IndicatorDetail("indicator-detail"),
    Signup("signup"),
    PasswordReset("password-reset"),
    ReportStory("report-story");

    fun toDeepLink(): String = "sarangsho://app/$route"

    companion object {
        private val byRoute = entries.associateBy(AppDestination::route)

        fun fromRoute(route: String?): AppDestination? = route?.let(byRoute::get)

        fun fromDeepLink(value: String?): AppDestination? {
            if (value.isNullOrBlank()) return null
            return runCatching {
                val uri = URI(value)
                if (uri.scheme != "sarangsho" || uri.host != "app" ||
                    uri.rawQuery != null || uri.rawFragment != null
                ) return null
                val path = uri.rawPath ?: return null
                if (!path.startsWith("/") || path.count { it == '/' } != 1) return null
                fromRoute(path.removePrefix("/"))
            }.getOrNull()
        }
    }
}
