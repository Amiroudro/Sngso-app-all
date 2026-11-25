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
- Debug APK artifact: `sarangsho-plus-debug-apk` (16,550,301 bytes). GitHub Actions retention is one day; download it from the run's Artifacts section before it expires.
- Emulator job: **failure**, with `tests=0`. The AOSP API 35 emulator booted and installed both APKs, but the app process failed to complete startup and was ANR-killed before instrumentation could execute.
- Captured emulator diagnostics show CPU pressure `some avg10=88.08`, `avg60=88.47`, `avg300=74.20`, and 99% total guest CPU use. Android system services also experienced ANRs. The evidence points to severe TCG software-emulation CPU saturation; it does not establish an application-code crash.
- The next CI attempt uses an API 30 AOSP image and four virtual CPU cores to test whether a lighter guest environment can complete instrumentation.

The routine job confirms compilation and packaging, not runtime UI behavior. The 37 authored Android instrumentation tests remain unexecuted until an emulator run completes successfully. No physical-device or visual-accessibility result is claimed.
