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

Latest verification on the current branch head before the next prepared change:

- Workflow run: [45651907054](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45651907054)
- Tested commit: `3dcce6a79f59fba0a61f58a6df06e065543eb8aa`
- Routine job: **success**. Toolchain verification, JVM unit tests, instrumentation-test APK compilation, debug APK assembly and artifact upload passed.
- Debug APK: 17,024,591 bytes; SHA-256 `6cb67a89cef9f87b2c12ea46ad1e90c333069f8b81ff5e1dad6fab3ffa0734b1`. Artifact ZIP: `sarangsho-plus-debug-apk` (16,550,302 bytes), retained for one day.
- Emulator job: **failure**, with zero app tests executed. SDK Platform 30 was installed and the API 30 AOSP emulator connected, but Android guest services became unresponsive. Logcat records an ANR in `com.android.phone`, repeated slow `system_server` operations and a 19.5-second foreground-handler dispatch. Gradle's device-property query raised `ShellCommandUnresponsiveException`, then reported `Unknown API Level`. The generated report is a synthetic `No compatible devices connected` failure, not an app test failure.
- The earlier API 35 AOSP attempt [45644373497](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45644373497) reached app startup but ANR-killed the app under severe TCG CPU pressure (88% CPU pressure avg10; 99% total guest CPU use).
- The next diagnostic configuration uses API 28 AOSP and two virtual CPU cores, with an explicit guest-shell readiness check before Gradle runs. This is an environment experiment, not evidence of runtime test success.

The routine job confirms compilation and packaging, not runtime UI behavior. The 37 authored Android instrumentation tests remain unexecuted until an emulator run completes successfully. No physical-device or visual-accessibility result is claimed.
