# UI quality audit

**Scope:** source-level review of the current Compose prototype. This is not a rendered-device audit. No visual score is assigned until screenshots can be inspected at real device sizes.

## Verified in source

- 47 route entries each have a matching navigation branch and screen function.
- Five primary destinations remain in bottom navigation.
- Shared screen components are separated from reading, learning and settings feature files.
- Semantic light and dark color schemes are centralized.
- Search and category filters work against explicitly labelled local sample stories.
- Compose instrumentation tests cover the onboarding path and basic local learning-store add/read/remove behavior. These tests have been authored but have not yet run successfully in CI.
- Vocabulary Lab has a searchable five-entry sample word list with per-word bookmark controls; bookmarks and the fixed study-plan checklist are stored locally with Android SharedPreferences. The Saved screen lists/removes bookmarked words and refreshes when returning to either screen. This is device-only and does not sync.
- Vocabulary definitions and examples are prototype learning content; dictionary/source references and a maintained exam vocabulary feed are not connected.
- Authentication, saved articles, downloads, feedback submission and user analytics are not represented as working services.
- Empty and unavailable states explain when their data/service is not connected.
- Sample stories and sample vocabulary are labelled as such.
- Routine pull-request CI runs unit tests and debug APK assembly; emulator instrumentation is reserved for manual workflow dispatch to conserve Actions minutes.

## Release blockers

| Priority | Finding | Required evidence to close |
|---|---|---|
| P0 | Android build and unit tests have not completed successfully yet | Green unit-test and debug-assembly workflow |
| P0 | No successful emulator instrumentation or real-device visual verification | Manual emulator workflow passes; screenshots from a small phone, large phone and tablet |
| P1 | Gradle Wrapper files are now committed and CI uses them, but the wrapper has not yet completed a fresh build in this environment | Verify wrapper download, Gradle 8.9 startup, unit tests and APK assembly |
| P1 | Editorial engine contract is not connected | Contract tests against approved edition fixtures |
| P1 | Vocabulary bookmarks and the fixed study checklist have basic device-local persistence; saved articles, settings, dated plans and practice history are not persisted | Room/DataStore implementation, repository tests and migration tests |
| P1 | Vocabulary entries are a small manually authored sample without dictionary/source citations or feed updates | Add reviewed word records with source metadata, provenance and update validation |
| P1 | Authentication and account recovery are unavailable | Real auth integration or remove account creation from release UI |
| P1 | Offline edition download/sync is unavailable | Download lifecycle, storage errors and stale-cache tests |
| P2 | Screen routes are still string-based | Move to typed route definitions before route count expands further |
| P2 | Some UI state is local to composables | Move durable/business state to ViewModels and collect lifecycle-aware state |
| P2 | Bengali font scaling and TalkBack have not been tested on hardware | Manual accessibility pass at default and enlarged text scales |
| P2 | Dark theme has source support but no rendered contrast audit | Capture and inspect dark-mode screens |

## Next engineering sequence

1. Get the routine Android unit/build workflow green and fix compiler/test failures before adding more UI.
2. Run the emulator instrumentation workflow manually after the unit/build gate passes.
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
- Primary navigation is limited to five destinations.
- Unsupported actions are disabled with explanatory copy rather than appearing functional.
- No live story count, source comparison, score, progress metric or download state is fabricated.
## Navigation source check (2026-11-19)

A source-level scan of the current branch found:

- 47 unique route declarations and 47 matching screen-dispatch branches.
- 65 literal `navigate("…")` calls across the activity and feature files.
- No literal navigation target points to an undeclared route.
- `word-detail` and `language` currently have no literal incoming navigation call. They are reachable only if a future/dynamic entry point is added, or they may be obsolete; confirm intended entry points before release.

This checks string consistency only. It does not prove that every screen renders, that every interaction works, or that navigation survives process recreation. A compiled build and device-level tests are still required.
