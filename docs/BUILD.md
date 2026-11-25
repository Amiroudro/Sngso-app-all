# Android build guide

## Required tools

- JDK 17
- Android SDK Platform 35
- Android SDK Build-Tools 35.0.0
- Android SDK Platform-Tools

Gradle itself does not need to be installed. The committed Gradle Wrapper downloads the pinned Gradle 8.9 distribution.

## Build and test

On macOS or Linux:

```sh
./gradlew --version
./gradlew --no-daemon :app:testDebugUnitTest
./gradlew --no-daemon :app:assembleDebugAndroidTest
./gradlew --no-daemon :app:assembleDebug
```

On Windows:

```bat
gradlew.bat --version
gradlew.bat --no-daemon :app:testDebugUnitTest
gradlew.bat --no-daemon :app:assembleDebugAndroidTest
gradlew.bat --no-daemon :app:assembleDebug
```

The `assembleDebugAndroidTest` task compiles and packages the instrumentation test APK. It does not execute the tests. To execute them, start an Android emulator with API 30 or newer first, then run:

```sh
./gradlew --no-daemon :app:connectedDebugAndroidTest
```

The debug app APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## GitHub Actions usage

- Pull-request updates run unit tests, compile/package the instrumentation test APK, and assemble the debug app APK.
- The workflow does not run a second time for branch pushes while a pull request is open.
- A concurrency group cancels an older in-progress verification for the same pull request when a newer commit arrives.
- Emulator instrumentation runs for non-draft pull requests after the routine unit/build job succeeds. The hosted Linux runner currently uses TCG software emulation because KVM is unavailable.
- The `workflow_dispatch` trigger is retained for deliberate manual reruns after the workflow exists on the default branch. GitHub requires manually dispatched workflows to exist on that branch.
- APK artifacts are retained for one day to limit storage usage. Download a needed APK promptly after a successful run.

## Reproducibility and integrity

- Gradle distribution: 8.9 binary ZIP
- Distribution SHA-256: `d725d707bfabd4dfdc958c624003b3c80accc03f7037b5122c4b1d0ef15cecab`
- Wrapper JAR: sourced from the Gradle 8.9.0 release tag
- Official Gradle checksum reference: https://gradle.org/release-checksums/

The wrapper verifies the downloaded Gradle distribution against the pinned SHA-256 before using it.

## Current verification status

Latest completed emulator verification:

- Workflow run: [45655470935](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45655470935)
- Tested commit: `cdcb28ac682f071b2bafa85bea175bed25ac8e4a`
- Routine job: **success**. Toolchain verification, JVM unit tests, compilation/package of all 39 authored Android instrumentation tests, debug APK assembly and artifact upload passed.
- Debug APK: 17,024,591 bytes; SHA-256 `05bb40614620f8d2dfc933e99e4d6dd85a96509abd08d4cdae344dd540cfe6fd`. Artifact ZIP: 16,548,380 bytes, retained for one day.
- Emulator: API 28 AOSP booted and executed the complete 39-test instrumentation suite. **21 passed, 18 failed.** The privacy-manifest test and 19 of 20 local-store tests passed. Seventeen HomeScreen tests failed because the shared onboarding test helper attempted to tap the final button without scrolling it into view; this left tests on the onboarding screen. The profile test also matched two identical profile headings. One local-store migration test expected spaces in a JSON string, while `JSONArray.toString()` emits compact JSON.
- No instrumentation process crash prevented test execution. The failures are now reproducible test interaction/assertion issues, not emulator boot failures.
- Commit `7617b21` adds scroll-to-visible actions to onboarding and Home test targets, disambiguates the profile assertion, fixes the compact JSON expectation, and makes the unloaded Edition screen show an explicit placeholder instead of opening an unrelated sample article. A new workflow run will verify these changes.

The debug APK is suitable for local UI exploration, but the app is not release-signed and the instrumentation suite is not yet green. No physical-device or visual-accessibility result is claimed.
