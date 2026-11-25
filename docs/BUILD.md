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

- Workflow run: [45658108951](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45658108951)
- Tested commit: `e8d960f2e23f96f27722168b0a87ee9036721b31`
- Routine job: **success**. Toolchain verification, JVM unit tests, compilation/package of all 39 authored Android instrumentation tests, debug APK assembly and artifact upload passed.
- Debug APK: 17,024,591 bytes; SHA-256 `bbf159d7ecb56aa74e4cd5d2dd5f6918b2b047a350152c78bd5cae5ef08abd54`. Artifact ZIP: 16,548,605 bytes, retained for one day.
- Emulator: API 28 AOSP executed all 39 instrumentation tests. **32 passed, 7 failed.** The remaining failures are UI test interaction/selector issues: Explore's first story title was off-screen; Saved Collections and both reader-preference tests tapped the off-screen Home article button without scrolling; the Learn practice button and Library search field also needed scrolling; one test matched two identical practice-history headings.
- The privacy-manifest test and all 20 local-store tests passed. No emulator startup failure prevented test execution.
- Commit `43ec7e8` adds scroll-to-visible behavior to the remaining off-screen test targets and selects one of the duplicate practice-history headings. Commit `a337143` also replaces the unloaded Edition screen's unrelated sample-article link with an explicit placeholder. The next workflow run will verify these changes.

The current branch adds a local-only story-correction draft and its persistence regression test, bringing the authored Android instrumentation inventory to 40. The latest completed emulator run predates that test addition. The debug APK is suitable for local UI exploration, but it is not release-signed and the instrumentation suite is not yet green. No physical-device or visual-accessibility result is claimed.
