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

The `assembleDebugAndroidTest` task compiles and packages the instrumentation test APK. It does not execute the tests. To execute them, start an Android 35 emulator first, then run:

```sh
./gradlew --no-daemon :app:connectedDebugAndroidTest
```

The debug app APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## GitHub Actions usage

- Pull-request updates run unit tests, compile/package the instrumentation test APK, and assemble the debug app APK.
- The workflow does not run a second time for branch pushes while a pull request is open.
- A concurrency group cancels an older in-progress verification for the same pull request when a newer commit arrives.
- Emulator instrumentation is deliberately gated. On an unmerged draft PR, it runs only when the PR is marked ready for review, and only after the routine unit/build job succeeds.
- The `workflow_dispatch` trigger is retained for deliberate manual reruns after the workflow exists on the default branch. GitHub requires manually dispatched workflows to exist on that branch.
- APK artifacts are retained for one day to limit storage usage. Download a needed APK promptly after a successful run.

## Reproducibility and integrity

- Gradle distribution: 8.9 binary ZIP
- Distribution SHA-256: `d725d707bfabd4dfdc958c624003b3c80accc03f7037b5122c4b1d0ef15cecab`
- Wrapper JAR: sourced from the Gradle 8.9.0 release tag
- Official Gradle checksum reference: https://gradle.org/release-checksums/

The wrapper verifies the downloaded Gradle distribution against the pinned SHA-256 before using it.

## Current verification status

Latest successful routine pull-request build:

- Workflow run: [45619100750](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45619100750)
- Verified branch commit: `a875feda7c4cbe9dd824c4d904977bb473854946`
- Result: **success**
- Passed steps: toolchain verification, JVM unit tests, instrumentation-test APK compilation, debug APK assembly and artifact upload.
- Debug APK artifact: `sarangsho-plus-debug-apk` (16,550,308 bytes). GitHub Actions retention is one day; download it from the run's Artifacts section before it expires.

This routine workflow confirms that the project compiles and packages the app and instrumentation APKs. It does not execute the 37 authored Android instrumentation tests. The emulator job was skipped because the pull request remains a draft. No emulator or physical-device runtime result is claimed.
