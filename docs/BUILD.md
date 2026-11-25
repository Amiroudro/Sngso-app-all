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

Latest verification on the current branch head:

- Workflow run: [45644373497](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45644373497)
- Branch commit: `964d16d824dace81ddaa1e539d8c5c5c762eecad`
- Routine job: **success**. Toolchain verification, JVM unit tests, instrumentation-test APK compilation, debug APK assembly and artifact upload all passed.
- Debug APK: 17,024,591 bytes; SHA-256 `6cb67a89cef9f87b2c12ea46ad1e90c333069f8b81ff5e1dad6fab3ffa0734b1`. Artifact ZIP: `sarangsho-plus-debug-apk` (16,550,300 bytes), retained for one day.
- Emulator job: **failure**, with zero app tests executed. The API 30 AOSP emulator connected, but Gradle rejected it as `Unknown API Level` because the emulator job had installed SDK Platform 35 only; Platform 30 was missing. The generated report contains one synthetic `No compatible devices connected` failure, not an app test failure.
- The prior API 35 AOSP attempt (run [45644373497](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45644373497)) reached instrumentation but ANR-killed the app under severe TCG CPU pressure (88% CPU pressure avg10; 99% total guest CPU use).
- The next CI attempt installs both SDK Platforms 30 and 35 and sets the emulator runner's dedicated `cores: 4` input. The previous `-cores 4` option alone did not override the action's two-core configuration.

The routine job confirms compilation and packaging, not runtime UI behavior. The 37 authored Android instrumentation tests remain unexecuted until an emulator run completes successfully. No physical-device or visual-accessibility result is claimed.
