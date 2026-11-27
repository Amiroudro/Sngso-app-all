# Sarangsho Plus Android

A Bangla-first editorial intelligence and exam-preparation app for Bangladesh.

This repository contains the Android client. The editorial engine lives separately in [Amiroudro/new-sngso](https://github.com/Amiroudro/new-sngso). The app now reads published daily editions from the public [Sarangsho Plus editions feed](https://github.com/Amiroudro/sarangsho-plus-editions), including the Bengali overlay and publisher source links. It caches the last valid edition for offline reading. It does not scrape publishers or generate editorial claims itself.

## Product experience

Sarangsho Plus helps readers move from a verified news event to understanding, context, and exam practice.

The current Compose prototype contains 48 navigation destinations across:
- First-run goals and topic interests
- Today briefing, Learn hub, feed, archive, edition and article reading
- Three-depth summaries, timelines, publication perspectives, sources
- Bangladesh and world affairs, topic detail, knowledge graph and data explorer
- Vocabulary, practice setup, question session, answer feedback and result shell
- Written practice, revision deck and study planning
- Search, saved items, notifications and profile
- Reading preferences, downloads, settings, help, about and feedback

This is an expanding screen inventory, not a required count. A route is added only when it represents a distinct user task or a necessary state. Related content should remain in a focused detail section when that is clearer than another page.

## Technical direction

- Kotlin + Jetpack Compose
- Material 3 and Navigation Compose
- Kotlin 2.0.21, Android Gradle Plugin 8.7.3, Gradle 8.9, JDK 17
- Semantic light/dark theme tokens in SarangshoTheme.kt
- Unit and Compose instrumentation test dependencies
- GitHub Actions build, unit-test and emulator smoke-test workflow

Open the project in Android Studio, allow Gradle sync and run the app configuration on a device or emulator. The Gradle Wrapper is committed and the GitHub Actions workflow uses it. The routine pull-request workflow runs JVM tests, compiles the Android instrumentation test APK, and assembles debug and unsigned release APKs. A separate emulator job executes the instrumentation suite on API 28 AOSP using software CPU emulation. Latest verification: [GitHub Actions run 45984631554](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45984631554) on commit `9553c45c9bd834d798170556cfa8ad46698f5100`; all 46 instrumentation tests passed (0 failures, 0 skipped), and debug plus unsigned release APKs were assembled. The run includes the adaptive launcher-icon regression test.

## Design principles

- Bengali-first typography with readable line-height choices; font fallback, enlarged text and device rendering still require a hardware accessibility pass.
- A restrained ink / warm-paper / deep-teal palette; no generic gradient-heavy dashboard.
- Strong editorial hierarchy: headlines, metadata, evidence, context and learning actions remain distinct.
- Reading surfaces prioritize line length, contrast, spacing and adjustable text size.
- Charts show units, periods, source and whether values are preliminary or revised.
- Never display invented progress, source comparisons, ratings or learning analytics.
- Every interactive control must work or be visibly disabled with a clear reason.
- Respect system font scaling, reduced motion, TalkBack semantics and 48dp touch targets.

## Product and UX documentation

- [Product brief](PRODUCT.md)
- [Design system](DESIGN.md)
- [Screen architecture](docs/SCREEN_ARCHITECTURE.md)
- [UX state contract](docs/UX_STATES.md)

## Current limitations

The UI remains an evolving product build. The Home, Feed and Article screens consume the published daily-edition feed with a 15-minute cache freshness window, manual refresh, and visible offline fallback. Account authentication, cloud sync, push delivery, live multi-publication perspective analysis, interactive knowledge-graph data and production analytics are not connected. The new engine's separate client-edition v1 projection is not yet wired to the production publisher workflow. The adaptive launcher icon is implemented, but physical-device visual and accessibility checks remain outstanding. Several learning and reader features persist locally; this is not account sync. Build status must be taken from GitHub Actions, not inferred from committed source.

## Related projects and guidance

- Editorial engine: [Amiroudro/new-sngso](https://github.com/Amiroudro/new-sngso)
- [UI UX Pro Max](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill)
- [Impeccable](https://github.com/pbakaus/impeccable)
