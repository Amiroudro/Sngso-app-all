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

- Pull-request updates run JVM unit tests, compile/package the instrumentation test APK, and assemble both debug and unsigned release APKs. The workflow uploads the unsigned release APK for build inspection; it is not signed for normal distribution.
- The workflow does not run a second time for branch pushes while a pull request is open.
- A concurrency group cancels an older in-progress verification for the same pull request when a newer commit arrives.
- Emulator instrumentation runs for non-draft pull requests after the routine unit/build job succeeds. The hosted Linux runner currently uses TCG software emulation because KVM is unavailable.
- The `workflow_dispatch` trigger is retained for deliberate manual reruns after the workflow exists on the default branch. GitHub requires manually dispatched workflows to exist on that branch.
- Debug APK artifacts are retained for one day. Unsigned release APK and emulator diagnostics artifacts are retained for seven days. Download needed artifacts promptly after a successful run.

## Reproducibility and integrity

- Gradle distribution: 8.9 binary ZIP
- Distribution SHA-256: `d725d707bfabd4dfdc958c624003b3c80accc03f7037b5122c4b1d0ef15cecab`
- Wrapper JAR: sourced from the Gradle 8.9.0 release tag
- Official Gradle checksum reference: https://gradle.org/release-checksums/

The wrapper verifies the downloaded Gradle distribution against the pinned SHA-256 before using it.

## Current verification status

Latest completed verification:

- Workflow run: [45856677746](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45856677746)
- Tested commit: `8ec761a73390bba63ae94c286a25bb1b89ecb739`
- Overall workflow: **success**. Both `unit-and-build` and `compose-instrumentation` jobs passed.
- Routine job: JVM unit tests, instrumentation APK compilation, debug APK assembly, and unsigned release APK assembly all passed. Both APK artifacts uploaded successfully.
- Emulator: API 28 AOSP completed all 42 authored Android instrumentation tests. **42 completed, 0 failed, 0 skipped.**
- Unsigned release APK artifact: 11,105,551 bytes; retained for seven days. Debug APK artifact: 17,176,246 bytes; retained for one day. Emulator diagnostics artifact: retained for seven days.
- Build output includes Compose icon API deprecation warnings in `ReadingScreens.kt`; they did not fail the build and remain cleanup work.

The unsigned release APK is a build-verification artifact, not a signed distribution package. The debug APK is suitable for local UI exploration, but it is not release-signed. No physical-device, rendered visual or accessibility result is claimed.

## Direct-distribution release APK

The app has a release build type and a separate manually triggered workflow at
`.github/workflows/release-apk.yml`. The workflow builds a signed APK, verifies
its APK signature with `apksigner`, writes a SHA-256 checksum, and uploads the
APK as a 30-day artifact. It does not publish the APK to a store.

### Create and protect the signing key

Create the keystore once on a trusted computer with JDK 17 or newer:

```sh
keytool -genkeypair -v -keystore sarangsho-release.jks -alias sarangsho -keyalg RSA -keysize 4096 -validity 10000
```

Choose strong, unique keystore and key passwords. Keep the keystore and its
passwords in a secure offline backup. **Losing this key can prevent future
updates from being accepted as updates to the same installed app. Never commit
the keystore, passwords, or an unencrypted copy to this repository.**

Convert the keystore to a single-line Base64 value:

- macOS/Linux: `base64 -w 0 sarangsho-release.jks` (on macOS use `base64 < sarangsho-release.jks | tr -d '\\n'`)
- Windows PowerShell: `[Convert]::ToBase64String([IO.File]::ReadAllBytes("sarangsho-release.jks"))`

In the GitHub repository, open **Settings → Secrets and variables → Actions**
and create these repository secrets:

| Secret | Value |
|---|---|
| `SNGSO_KEYSTORE_BASE64` | The complete Base64 output |
| `SNGSO_KEYSTORE_PASSWORD` | Keystore password |
| `SNGSO_KEY_ALIAS` | `sarangsho` (or the alias you chose) |
| `SNGSO_KEY_PASSWORD` | Key password |

After this workflow file is present on the repository's default branch, open
**Actions → Direct APK Release → Run workflow**. GitHub's manual workflow
dispatch requires the workflow to exist on the default branch. The release job
fails closed if any signing secret is missing and does not fall back to an
unsigned APK.

Download the `sarangsho-plus-direct-release` artifact from the successful run.
Verify the accompanying `.sha256` file before sharing the APK. Install and
smoke-test the release APK on a physical Android device before wider
distribution. Do not share the keystore or signing secrets with testers.
