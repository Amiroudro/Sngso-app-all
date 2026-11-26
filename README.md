# Sarangsho Plus Android

A Bangla-first editorial intelligence and exam-preparation app for Bangladesh.

This repository contains the Android client. The editorial engine lives separately in [Amiroudro/new-sngso](https://github.com/Amiroudro/new-sngso). The client consumes versioned, validated edition artifacts; it does not scrape publishers or generate editorial claims itself.

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

Open the project in Android Studio, allow Gradle sync and run the app configuration on a device or emulator. The Gradle Wrapper is committed and the GitHub Actions workflow uses it. The routine pull-request workflow runs JVM tests, compiles the Android instrumentation test APK, and assembles debug and unsigned release APKs. A separate emulator job executes the instrumentation suite on API 28 AOSP using software CPU emulation. Latest verification: [GitHub Actions run 45888948067](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45888948067), with all 43 instrumentation tests passing (0 failures, 0 skipped).

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

The UI is still a prototype. Authentication, cloud sync, engine/API integration, offline edition downloads, push delivery, live source comparisons, interactive knowledge-graph data and production analytics are not connected. Several learning and reader features do persist locally with SharedPreferences; this is not account sync or a production data layer. Sample content is labelled as sample. Build status must be taken from GitHub Actions, not inferred from committed source.

## Related projects and guidance

- Editorial engine: [Amiroudro/new-sngso](https://github.com/Amiroudro/new-sngso)
- [UI UX Pro Max](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill)
- [Impeccable](https://github.com/pbakaus/impeccable)
