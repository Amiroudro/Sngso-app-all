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

- Workflow run: [45654046195](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45654046195)
- Tested commit: `e2ef5baedca5003e878ff3844669544df57192c2`
- Routine job: **success**. Toolchain verification, JVM unit tests, compilation/package of all 39 authored Android instrumentation tests, debug APK assembly and artifact upload passed.
- Debug APK: 17,024,591 bytes; SHA-256 `4b51a5bb8bbfa4f0234a213c0720580761a3b3464261e34b36aedab2f8b58a8e`. Artifact ZIP: `sarangsho-plus-debug-apk` (16,551,113 bytes), retained for one day.
- Emulator job: **failure**, with zero app tests executed. The API 28 AOSP emulator booted successfully in approximately 151 seconds. The emulator action then invoked the multiline shell preflight, but its script runner parsed the `for` loop as an incomplete command and exited with `Syntax error: end of file unexpected (expecting "done")` before Gradle or Logcat capture began. This is a workflow-script error, not an app test failure.
- Earlier API 30 attempts failed because the guest was unresponsive during Gradle's device-property query; the API 35 attempt suffered severe TCG CPU pressure and an app-startup ANR.
- The next attempt keeps API 28 AOSP and two virtual cores, removes the unnecessary multiline preflight, and proceeds directly to Logcat capture and `:app:connectedDebugAndroidTest`.

The routine job confirms compilation and packaging, not runtime UI behavior. The 39 authored Android instrumentation tests remain unexecuted until an emulator run completes successfully. No physical-device or visual-accessibility result is claimed.
