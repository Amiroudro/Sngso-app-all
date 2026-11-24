# UI quality audit

**Scope:** source-level review of the current Compose prototype. This is not a rendered-device audit. No visual score is assigned until screenshots can be inspected at real device sizes.

## Verified in source

- 48 route entries each have a matching navigation branch and screen function.
- Four primary destinations remain in bottom navigation: Today, Learn, Library and Explore.
- Shared screen components are separated from reading, learning and settings feature files.
- Semantic light and dark color schemes are centralized.
- Search and category filters work against explicitly labelled local sample stories.
- Compose instrumentation tests cover onboarding, Explore filtering, Library empty/search states, article and vocabulary bookmarks, the Learn-hub activity shortcuts, the quiz-to-history journey, and local learning-store persistence. Their APK compiles in CI, but the instrumentation tests have not been executed on an emulator.
- The routine pull-request workflow is configured to compile and package the instrumentation test APK using `:app:assembleDebugAndroidTest`. This checks that test sources compile; it does not execute the instrumentation tests.
- Emulator instrumentation is gated to the PR's ready-for-review event after the routine build job succeeds, with manual dispatch retained for use after the workflow reaches the default branch.
- Vocabulary Lab has a searchable five-entry sample word list with per-word bookmark controls. Saved words and articles, reader and language preferences, onboarding choices, notification preferences, writing and feedback drafts, reading history, practice history, and the study checklist are stored locally with Android SharedPreferences. The study checklist resets by local calendar date. These features are device-only and do not sync.
- Vocabulary definitions and examples are prototype learning content; dictionary/source references and a maintained exam vocabulary feed are not connected.
- Authentication, downloads, feedback submission and production user analytics are not connected. Saved articles are stored locally on-device; they are not account-synced.
- Empty and unavailable states explain when their data/service is not connected.
- Sample stories and sample vocabulary are labelled as such.

## Release blockers

| Priority | Finding | Required evidence to close |
|---|---|---|
| P0 | Basic Android buildability is verified, but must remain stable | Keep the routine unit-test, instrumentation-APK compilation and debug-assembly workflow green on every source change |
| P0 | No successful emulator instrumentation or real-device visual verification | Emulator workflow passes; screenshots from a small phone, large phone and tablet |
| P1 | Gradle Wrapper has completed a fresh CI build, but local developer setup is not yet documented end-to-end | Verify wrapper download, Gradle startup and APK assembly on a clean developer machine |
| P1 | Editorial engine contract is not connected | Contract tests against approved edition fixtures |
| P1 | Learning and reader data currently use device-local SharedPreferences rather than a structured database | Move durable collections and preferences to Room/DataStore with repository, migration, backup/restore and process-death tests |
| P1 | Vocabulary entries are a small manually authored sample without dictionary/source citations or feed updates | Add reviewed word records with source metadata, provenance and update validation |
| P1 | Authentication and account recovery are unavailable | Real auth integration or remove account creation from release UI |
| P1 | Offline edition download/sync is unavailable | Download lifecycle, storage errors and stale-cache tests |
| P2 | Screen routes are still string-based | Move to typed route definitions before route count expands further |
| P2 | Some UI state is local to composables | Move durable/business state to ViewModels and collect lifecycle-aware state |
| P2 | Bengali font scaling and TalkBack have not been tested on hardware | Manual accessibility pass at default and enlarged text scales |
| P2 | Dark theme has source support but no rendered contrast audit | Capture and inspect dark-mode screens |

## Next engineering sequence

1. Get the routine Android unit/build and instrumentation-test compilation workflow green; fix compiler/test-source failures before adding more UI.
2. Run the emulator instrumentation workflow only after the routine build succeeds.
3. Replace string routes with typed destinations and add deep-link contracts.
4. Introduce screen-level ViewModels and immutable UI state.
5. Connect approved edition fixtures, then the real engine API.
6. Add reviewed, source-attributed vocabulary records and a controlled update path; replace the initial SharedPreferences store with Room/DataStore repositories for offline reading, saved items, preferences and learning progress.
7. Capture rendered screenshots and run the Impeccable critique/audit pass at real device sizes.
8. Complete Bengali typography, TalkBack, font scaling, dark theme and reduced-motion checks.

## Design review principles applied

- The repeated icon-card row pattern was replaced with editorial list rows.
- Reading screens use headline and metadata hierarchy rather than dashboard metrics.
- Shared colors come from semantic theme tokens.
- Primary navigation is limited to four destinations: Today, Learn, Library and Explore.
- Unsupported actions are disabled with explanatory copy rather than appearing functional.
- No live story count, source comparison, score, progress metric or download state is fabricated.

## Navigation source check (updated 2026-11-24)

A source-level scan of the current branch found:

- 48 unique route declarations and 48 matching screen-dispatch branches.
- 90 literal `navigate("…")` calls across the activity and feature files.
- No literal navigation target points to an undeclared route.
- `word-detail` and `language` currently have no literal incoming navigation call. They are reachable only if a future/dynamic entry point is added, or they may be obsolete; confirm intended entry points before release.

This checks string consistency only. It does not prove that every screen renders, that every interaction works, or that navigation survives process recreation. A compiled build and device-level tests are still required.

## Test coverage inventory (updated 2026-11-24)

A source review of the test files found 23 authored Android instrumentation test methods:

| Test file | Test methods | What they cover |
|---|---:|---|
| `HomeScreenTest.kt` | 8 | Welcome, onboarding, Explore filtering, Library empty/search states, article and vocabulary bookmarks, Learn-hub shortcuts, and the sample quiz-to-history journey |
| `LocalLearningStoreTest.kt` | 15 | Topic, vocabulary search, bookmarks, drafts, reader preferences, onboarding and notification preferences, reading history ordering/migration, practice history, and daily study-task persistence/reset |

Coverage limits:

- These are Android instrumentation tests, not JVM unit tests.
- All 15 local-store tests clear their test preferences before and after each test.
- The test source uses Compose UI test APIs and AndroidX instrumentation APIs declared in the app module.
- No test currently verifies all 48 routes, article/summary navigation comprehensively, Saved-screen removal, process recreation, or accessibility behavior.
- The tests are authored only. CI has compiled the instrumentation APK successfully, but the tests have not yet been executed on an emulator. Compilation is not a passing runtime test.
- The routine CI step `:app:assembleDebugAndroidTest` only compiles/packages the instrumentation APK. It is not a substitute for `:app:connectedDebugAndroidTest`.

The next test expansion should follow actual user journeys and stable data contracts, not attempt to create one shallow test for every screen.
