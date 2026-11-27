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

- Workflow run: [45970572515](https://github.com/Amiroudro/Sngso-app-all/actions/runs/45970572515)
- Branch: `fix/android-build-verification`
- PR head commit: `6042b1c005521e15c5f327cf05e360756a4e8041` (GitHub Actions checks out the PR merge ref for the workflow).
- Overall workflow: **success**. Both `unit-and-build` and `compose-instrumentation` jobs completed successfully.
- JVM unit tests passed; instrumentation APK compilation, debug APK assembly, and unsigned release APK assembly all passed.
- API 28 AOSP emulator executed all 44 authored Android instrumentation tests: **44 passed, 0 failed, 0 skipped**. The uploaded test report ends with `OK (44 tests)`.
- Debug APK: 17,709,246 bytes; SHA-256 `7ee2a6ca0203895c32592a3d3588466845266376a60c53518bd8d345c6ecbc8e`.
- Unsigned release APK: 11,622,158 bytes; SHA-256 `8a6910498e5995c6a3f954c74639a558b0bec4de59d4a87e8d8a4cb966bc1dc9`.
- Artifact ZIP SHA-256 values: debug `7420e392696d604dcb7d28af991f9d78bddc706ac7a724265aad68eadfd32234`; unsigned release `eeaa672785a8a405e8271b42393a9ccdcd344716280e5a11edbc141a4b450833`.
- Debug APK artifact retention is one day; unsigned release APK and emulator diagnostics are retained for seven days. Download artifacts promptly from the workflow run.
- The debug APK is for local testing, not distribution. The release APK is unsigned and is not suitable for distribution.
- No signed distribution APK has been generated. No physical-device visual or accessibility result is claimed.

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
