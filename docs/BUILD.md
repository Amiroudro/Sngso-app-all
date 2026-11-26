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

Latest completed verification:

- Workflow run: [45832521965](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45832521965)
- Tested commit: `443917301b481f72ed32213c03c9b7a6b9f673d0`
- Overall workflow: **success**. Both `unit-and-build` and `compose-instrumentation` jobs passed.
- Routine job: toolchain verification, JVM unit tests, instrumentation APK compilation, debug APK assembly and artifact upload all passed.
- Emulator: API 28 AOSP executed all 41 authored Android instrumentation tests. **41 passed, 0 failed, 0 skipped.** The Explore category-filter interaction test passed.
- Debug APK artifact: 16,553,492 bytes; retained for one day. Emulator diagnostics artifact: retained for seven days.
- The workflow's emulator script now runs the test command, captures its exit status and stops logcat within one shell. This fixes the prior false-negative where per-line shell execution lost `LOGCAT_PID` and `TEST_EXIT` even though Gradle reported `BUILD SUCCESSFUL` and the instrumentation runner reported `OK (41 tests)`.

The debug APK is suitable for local UI exploration, but it is not release-signed. No physical-device, rendered visual or accessibility result is claimed.
