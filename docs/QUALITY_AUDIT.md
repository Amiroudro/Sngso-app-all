# UI quality audit

**Scope:** source-level review of the current Compose prototype. This is not a rendered-device audit. No visual score is assigned until screenshots can be inspected at real device sizes.

## Verified in source

- 48 route entries each have a matching navigation branch and screen function.
- Four primary destinations remain in bottom navigation: Today, Learn, Library and Explore.
- Shared screen components are separated from reading, learning and settings feature files.
- Semantic light and dark color schemes are centralized.
- Search and category filters work against explicitly labelled local sample stories. In Explore, only the Gambia/Myanmar sample headline opens its matching article and summary; other sample headlines disclose that full reading pages are not yet available.
- Compose instrumentation tests cover onboarding, Explore filtering, Library empty/search states, article and vocabulary bookmarks, the Learn-hub activity shortcuts, language settings persistence, the quiz-to-history journey, reader font, line-spacing and sepia preference persistence through Article settings, and local learning-store persistence. The current-head API 28 AOSP emulator run (workflow `45888948067`) executed all 44 authored instrumentation tests: **44 passed, 0 failed, 0 skipped**. The previously failing Explore category-filter interaction now passes.
- The routine pull-request job runs the JVM test, compiles/packages the instrumentation APK using `:app:assembleDebugAndroidTest`, and assembles the debug APK. The separate `compose-instrumentation` job executes the instrumentation suite on an API 28 AOSP emulator; compilation alone is not runtime verification.
- Emulator instrumentation runs on non-draft pull-request updates after the routine build succeeds. The hosted runner uses TCG software CPU emulation. Earlier API 35 and API 30 attempts exposed guest-performance and SDK setup problems; the current API 28 configuration completed the full 44-test suite successfully in workflow `45888948067`. The emulator workflow's shell wrapper keeps PID and exit-code variables in one shell so logcat cleanup cannot mask the test result.
- Vocabulary Lab has a searchable five-entry sample word list with per-word bookmark controls; search matches the term, part of speech, pronunciation, Bengali meaning, definition, synonyms, antonym and example, trims surrounding whitespace, and supports bookmarking from the selected-word detail screen. Reader font size, line spacing and sepia preferences are stored in Preferences DataStore. A non-destructive, idempotent migration copies only those three settings from the legacy `sarangsho_learning` SharedPreferences file and deliberately retains the source file and unrelated values. Other learning data (saved words and articles, language and onboarding preferences, notification preferences, writing and feedback drafts, reading history, practice history, and the study checklist) remains in device-local SharedPreferences. The study checklist resets by local calendar date. These features are device-only and do not sync.
- Vocabulary definitions and examples are prototype learning content; dictionary/source references and a maintained exam vocabulary feed are not connected.
- Android Auto Backup is disabled for the prototype so local drafts, bookmarks and reading activity are not silently included in the platform backup flow. A deliberate export/restore feature is not implemented.
- Authentication, downloads, feedback submission and production user analytics are not connected. Saved articles are stored locally on-device; they are not account-synced.
- Article and three-level summary reading controls now refresh font size, line spacing and sepia mode when the reader returns from Reading Settings; this avoids leaving an already-open reading surface with stale preferences.
- Empty and unavailable states explain when their data/service is not connected.
- Sample stories and sample vocabulary are labelled as such.

## Release blockers

| Priority | Finding | Required evidence to close |
|---|---|---|
| P0 | Keep Android CI green on every source change | Require JVM tests, instrumentation-APK compilation, debug and unsigned release assembly, and emulator instrumentation to pass for the current head |
| P0 | No physical-device visual verification | Capture and inspect screenshots on a small phone, large phone and tablet; verify Bengali typography, TalkBack, font scaling and dark mode |
| P1 | Gradle Wrapper has completed a fresh CI build, but local developer setup is not yet documented end-to-end | Verify wrapper download, Gradle startup and APK assembly on a clean developer machine |
| P1 | Editorial engine contract is not connected | Contract tests against approved edition fixtures |
| P1 | Durable collections and most learning preferences still use device-local SharedPreferences; only reader display preferences have moved to DataStore | Move collections to Room and remaining preferences to DataStore through repositories; add non-destructive migration, backup/restore policy and process-death tests |
| P1 | Vocabulary entries are a small manually authored sample without dictionary/source citations or feed updates | Add reviewed word records with source metadata, provenance and update validation |
| P1 | Authentication and account recovery are unavailable | Real auth integration or remove account creation from release UI |
| P1 | Offline edition download/sync is unavailable | Download lifecycle, storage errors and stale-cache tests |
| P2 | Navigation still uses the Navigation Compose string API at the framework boundary | Keep typed `AppDestination` as the sole route registry; introduce typed argument routes only when screens require stable IDs or filters |
| P2 | Some UI state is local to composables | Move durable/business state to ViewModels and collect lifecycle-aware state |
| P2 | Bengali font scaling and TalkBack have not been tested on hardware | Manual accessibility pass at default and enlarged text scales |
| P2 | Dark theme has source support but no rendered contrast audit | Capture and inspect dark-mode screens |

## Next engineering sequence

1. **Current-head CI passed:** workflow `45888948067` for commit `1e0616608d146f8becb0d046bf24e0061a8ccc6f` completed both CI jobs successfully. The API 28 AOSP emulator ran all 44 instrumentation tests: 44 passed, 0 failed, 0 skipped. Repeat this gate for every subsequent source change.
2. **Completed on current head:** API 28 AOSP emulator suite passed all 44 instrumentation test methods with zero failures or skips in workflow `45888948067`.
3. **Completed:** centralize all 48 routes in typed `AppDestination` identifiers, migrate feature navigation call sites, and define a strict `sarangsho://app/{route}` deep-link contract. JVM tests cover route uniqueness, round-tripping and malformed-link rejection; the manifest exposes the app link. Navigation Compose still accepts route strings at its framework boundary.
4. Introduce screen-level ViewModels and immutable UI state.
5. Connect approved edition fixtures, then the real engine API.
6. Add reviewed, source-attributed vocabulary records and a controlled update path; migrate remaining SharedPreferences collections and preferences to Room/DataStore repositories for offline reading, saved items and learning progress. Reader display preferences already use DataStore with a non-destructive legacy migration. Preserve all existing user data through tested migrations.
7. Capture rendered screenshots and run the Impeccable critique/audit pass at real device sizes.
8. Complete Bengali typography, TalkBack, font scaling, dark theme and reduced-motion checks.

## Design review principles applied

- The repeated icon-card row pattern was replaced with editorial list rows.
- Reading screens use headline and metadata hierarchy rather than dashboard metrics.
- Shared colors come from semantic theme tokens.
- Primary navigation is limited to four destinations: Today, Learn, Library and Explore.
- Unsupported actions are disabled with explanatory copy rather than appearing functional.
- No live story count, source comparison, score, progress metric or download state is fabricated.

## Navigation source check (updated 2026-11-26)

A source-level scan of the current branch found:

- 48 unique route declarations and 48 matching screen-dispatch branches.
- 97 literal `navigate("…")` calls across the activity and feature files; no literal target points to an undeclared route.
- One variable-driven `navigate(target)` call powers bottom navigation. Its four possible values (`home`, `learn`, `saved`, `feed`) are all declared routes.
- The Language Settings screen is reachable from Settings, the edition archive from Today, and Word Detail from each Vocabulary Lab entry. The selected term is passed through the navigation back-stack entry and rendered from the bundled vocabulary record.

This source scan predates the typed-destination migration. `AppDestination` is now the canonical registry for all 48 routes, and the activity plus feature navigation call sites reference its typed entries. Navigation Compose still receives each destination's stable string route at its API boundary. The deep-link parser accepts only the exact `sarangsho://app/{known-route}` form and rejects user-info, ports, unknown routes, encoded route aliases, extra path segments, query strings and fragments. JVM tests exercise every destination's route and deep-link round trip plus malformed inputs. This does not prove every screen renders or every back-stack journey works; device-level navigation tests and process-recreation coverage remain outstanding.

## Test coverage inventory (updated 2026-11-26)

A source review found one JVM unit test (`ExampleUnitTest.applicationIdIsStable`) and 44 authored Android instrumentation test methods. The 44 Android tests are:

| Test file | Test methods | What they cover |
|---|---:|---|
| `HomeScreenTest.kt` | 20 | Welcome, first-run and returning-user onboarding; Explore filters, matching article navigation and unavailable sample-story states; Library empty/search states; article and vocabulary bookmarks; Saved Collections opening, refresh and removal; unknown saved article handling; Learn-hub shortcuts and practice history; archive navigation; language persistence; reader font, line-spacing and sepia preferences; vocabulary detail/search; and profile navigation state |
| `LocalLearningStoreTest.kt` | 22 | Topic, vocabulary search, bookmarks, writing/feedback/story-report drafts, reader preferences, onboarding and notification preferences, reading history ordering/migration/bounds, practice-history validation/bounds, and daily study-task persistence/reset |
| `AppPrivacyManifestTest.kt` | 1 | Confirms Android Auto Backup is disabled for the app package |
| `ReaderPreferencesMigrationTest.kt` | 1 | Confirms reader settings migrate from legacy SharedPreferences without deleting the source or unrelated values |

Coverage limits:

- These are Android instrumentation tests, not JVM unit tests.
- All 22 local-store tests clear their test preferences before and after each test.
- The test source uses Compose UI test APIs and AndroidX instrumentation APIs declared in the app module.
- No test currently verifies all 48 routes, all article/summary navigation paths, process recreation, or accessibility behavior.
- The current source contains 44 authored instrumentation tests (20 HomeScreen, 22 local-store, 1 privacy-manifest, 1 reader-preferences migration). The latest completed emulator run `45888948067` passed the prior 43-test suite; the added returning-user launch test is awaiting its own CI result. This is emulator evidence, not a physical-device visual or accessibility audit.
- The routine CI step `:app:assembleDebugAndroidTest` only compiles/packages the instrumentation APK. It is not a substitute for `:app:connectedDebugAndroidTest`.

The next test expansion should follow actual user journeys and stable data contracts, not attempt to create one shallow test for every screen.
