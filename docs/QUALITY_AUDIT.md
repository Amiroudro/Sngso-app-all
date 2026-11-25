# UI quality audit

**Scope:** source-level review of the current Compose prototype. This is not a rendered-device audit. No visual score is assigned until screenshots can be inspected at real device sizes.

## Verified in source

- 48 route entries each have a matching navigation branch and screen function.
- Four primary destinations remain in bottom navigation: Today, Learn, Library and Explore.
- Shared screen components are separated from reading, learning and settings feature files.
- Semantic light and dark color schemes are centralized.
- Search and category filters work against explicitly labelled local sample stories. In Explore, only the Gambia/Myanmar sample headline opens its matching article and summary; other sample headlines disclose that full reading pages are not yet available.
- Compose instrumentation tests cover onboarding, Explore filtering, Library empty/search states, article and vocabulary bookmarks, the Learn-hub activity shortcuts, language settings persistence, the quiz-to-history journey, reader font, line-spacing and sepia preference persistence through Article settings, and local learning-store persistence. Their APK compiles in CI. API 35 AOSP ANR-killed the app before tests. API 30 attempts failed on missing SDK and then an unresponsive guest. API 28 AOSP booted and executed all 39 tests: 21 passed and 18 failed. Most HomeScreen failures came from the onboarding helper not scrolling its final button into view; one profile assertion matched two headings, and one local-store test expected spaced JSON. The test fixes are in commit `7617b21` and await rerun.
- The routine pull-request workflow is configured to compile and package the instrumentation test APK using `:app:assembleDebugAndroidTest`. This checks that test sources compile; it does not execute the instrumentation tests.
- Emulator instrumentation runs on non-draft pull-request updates after the routine build succeeds. The current hosted runner uses TCG software CPU emulation. API 35 AOSP reached app startup but suffered severe guest CPU pressure. API 30 AOSP first lacked its SDK platform; after the platform was installed, guest system services became unresponsive. API 28 AOSP booted and ran all 39 tests, exposing 18 test-level failures. The next run verifies the onboarding scroll, profile selector, JSON expectation and unloaded-Edition placeholder fixes.
- Vocabulary Lab has a searchable five-entry sample word list with per-word bookmark controls; search matches the term, part of speech, pronunciation, Bengali meaning, definition, synonyms, antonym and example, trims surrounding whitespace, and supports bookmarking from the selected-word detail screen. Saved words and articles, reader and language preferences, onboarding choices, notification preferences, writing and feedback drafts, reading history, practice history, and the study checklist are stored locally with Android SharedPreferences. The study checklist resets by local calendar date. These features are device-only and do not sync.
- Vocabulary definitions and examples are prototype learning content; dictionary/source references and a maintained exam vocabulary feed are not connected.
- Android Auto Backup is disabled for the prototype so SharedPreferences drafts, bookmarks and reading activity are not silently included in the platform backup flow. A deliberate export/restore feature is not implemented.
- Authentication, downloads, feedback submission and production user analytics are not connected. Saved articles are stored locally on-device; they are not account-synced.
- Article and three-level summary reading controls now refresh font size, line spacing and sepia mode when the reader returns from Reading Settings; this avoids leaving an already-open reading surface with stale preferences.
- Empty and unavailable states explain when their data/service is not connected.
- Sample stories and sample vocabulary are labelled as such.

## Release blockers

| Priority | Finding | Required evidence to close |
|---|---|---|
| P0 | Current branch head must pass routine Android CI before buildability is claimed | Keep the JVM smoke test, instrumentation-APK compilation and debug-assembly workflow green on every source change |
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
2. Re-run the API 28 AOSP suite after the remaining Explore/Home/Learn/Library scroll and duplicate-heading fixes. Require all 39 test methods to pass and inspect any remaining per-test failures.
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

## Navigation source check (updated 2026-11-25)

A source-level scan of the current branch found:

- 48 unique route declarations and 48 matching screen-dispatch branches.
- 97 literal `navigate("…")` calls across the activity and feature files; no literal target points to an undeclared route.
- One variable-driven `navigate(target)` call powers bottom navigation. Its four possible values (`home`, `learn`, `saved`, `feed`) are all declared routes.
- The Language Settings screen is reachable from Settings, the edition archive from Today, and Word Detail from each Vocabulary Lab entry. The selected term is passed through the navigation back-stack entry and rendered from the bundled vocabulary record.

This checks string consistency only. It does not prove that every screen renders, that every interaction works, or that navigation survives process recreation. A compiled build and device-level tests are still required.

## Test coverage inventory (updated 2026-11-25)

A source review found one JVM unit test (`ExampleUnitTest.applicationIdIsStable`) and 39 authored Android instrumentation test methods. The 39 Android tests are:

| Test file | Test methods | What they cover |
|---|---:|---|
| `HomeScreenTest.kt` | 18 | Welcome and onboarding; Explore filters, matching article navigation and unavailable sample-story states; Library empty/search states; article and vocabulary bookmarks; Saved Collections opening, refresh and removal; unknown saved article handling; Learn-hub shortcuts and practice history; archive navigation; language persistence; reader font, line-spacing and sepia preferences; vocabulary detail/search; and profile navigation state |
| `LocalLearningStoreTest.kt` | 20 | Topic, vocabulary search, bookmarks, drafts, reader preferences, onboarding and notification preferences, reading history ordering/migration/bounds, practice-history validation/bounds, and daily study-task persistence/reset |
| `AppPrivacyManifestTest.kt` | 1 | Confirms Android Auto Backup is disabled for the app package |

Coverage limits:

- These are Android instrumentation tests, not JVM unit tests.
- All 20 local-store tests clear their test preferences before and after each test.
- The test source uses Compose UI test APIs and AndroidX instrumentation APIs declared in the app module.
- No test currently verifies all 48 routes, all article/summary navigation paths, process recreation, or accessibility behavior.
- The tests are authored and compile successfully. API 28 AOSP executed all 39 instrumentation tests: 32 passed and 7 failed. The privacy-manifest test and all 20 local-store tests passed. The remaining failures are confined to HomeScreen UI interactions: some article/story/search/practice targets were below the viewport and one practice-history heading matched twice. Commit `43ec7e8` adds scroll-to-visible actions and disambiguates that heading. Runtime verification remains incomplete until the next run confirms the fixes.
- The routine CI step `:app:assembleDebugAndroidTest` only compiles/packages the instrumentation APK. It is not a substitute for `:app:connectedDebugAndroidTest`.

The next test expansion should follow actual user journeys and stable data contracts, not attempt to create one shallow test for every screen.
