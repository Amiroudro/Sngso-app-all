# Engine Integration Readiness

**Status:** Android client is device-smoke-tested; production editorial integration is not ready to connect.

## Verified current state

- The Android client is a Kotlin/Jetpack Compose app with 48 registered destinations.
- The installed signed APK was manually tested by the project owner. First launch, onboarding, navigation, reading, search, learning and settings were reported working on a physical Android phone.
- Android CI and the signed release workflow have passed for the release commit.
- The Android repository README explicitly identifies the current content as labelled local sample content.
- The Android manifest currently declares the launcher activity and the `sarangsho://app/{route}` deep-link contract. It does not declare the Android `INTERNET` permission.
- The app module currently has no HTTP client, serialization library, or Room database dependency. Reader display preferences use DataStore; other prototype collections and learning records remain device-local.
- The editorial engine repository is a separate project. Its README describes a foundation milestone: versioned contracts, runtime schema validation, deterministic hashing and fail-closed permission evaluation exist, while collection, persistence adapters, editorial production and publication are not implemented.

## Contract facts that affect the client

The engine's v1 Edition schema requires:

- `schema_version` equal to `1.0.0`
- stable edition ID and edition key
- creation timestamp and publication status
- content hash and pipeline run ID
- story IDs
- at least one language variant

The status enum includes draft, in-review, approved, published, rejected and superseded. A consumer must not treat a draft or in-review edition as publishable content.

The Article contract contains source identity, canonical URI, title, language, discovery time and record status. The LearningObject contract includes a story ID, kind, language, review status, content and claim IDs. These are domain records, not yet a complete app-facing feed/API specification.

## Integration blockers

1. **No publishable fixture or exporter is currently documented as available.** The engine must first produce a deterministic, schema-valid approved/published Edition fixture with all referenced story and learning records.
2. **No app-facing delivery format has been agreed.** Define a small read-only client projection rather than coupling Compose screens directly to internal engine entities.
3. **No network/data layer exists in the Android app.** Add the Internet permission only with a reviewed transport implementation, typed DTOs, bounded timeouts, error handling and tests.
4. **No trustworthy content lifecycle exists in the app.** The client needs loading, fresh, stale-cache, empty, unavailable and invalid-content states. Invalid schema versions or hashes must fail closed.
5. **No offline edition store exists.** Choose a versioned cache and migration strategy before replacing the local sample repository.
6. **Editorial trust must remain visible.** Every story projection must preserve publisher attribution, timestamps, source links, review/publication state and correction/supersession metadata where supplied.

## Safe implementation sequence

### Gate 1: Engine-owned fixture

The engine repository should provide a small synthetic or explicitly approved fixture set: one published edition, two stories, bilingual summary variants, a timeline, source metadata and learning objects. Include a manifest with schema version and content hash. Validate these fixtures in engine CI. Do not use protected article text or assume publisher permission.

### Gate 2: Client projection contract

Agree on a versioned, read-only JSON projection for the app. It should include only fields the client renders, retain provenance and stable IDs, and reject unknown major versions. Keep the projection separate from the engine's internal persistence models.

### Gate 3: Android data boundary

Introduce repository interfaces and DTOs, a schema/contract validator, a transport adapter, and a local cache. Keep Compose screens dependent on repository state, not raw HTTP responses. Test malformed payloads, hash mismatch, unknown versions, partial references, timeouts, stale cache and process recreation.

### Gate 4: Controlled rollout

First ship fixture-backed integration in a non-production build. Then test against a staging artifact endpoint. Promote only after engine contract CI, Android unit tests, emulator tests and physical-device checks pass. Do not write to the legacy website or published-editions repository as part of this work.

## Acceptance criteria for the first connected build

- Only approved/published editions are displayed.
- Every visible article has stable identity, source attribution and publication metadata.
- Bengali and English variants resolve deterministically.
- Invalid hashes, missing references and unsupported major versions are rejected with a safe unavailable state.
- A previously cached good edition remains readable if refresh fails.
- No sample story is presented as live news.
- Search operates on the actual cached/published corpus and clearly distinguishes no results from unavailable data.
- Contract fixtures and Android tests run in CI.

## Scope and preservation

This document is an Android-side readiness audit only. It does not modify the editorial engine, its contracts, schedules, secrets, website, or published editions. No production endpoint, credentials, publisher permissions or live content are assumed.
