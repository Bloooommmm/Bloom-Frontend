# Bloom Mobile

<p align="center">
  <img src="https://raw.githubusercontent.com/Bloooommmm/.github/main/profile/assets/bloom-logo.svg" alt="Bloom" width="220">
  <br>
  <img src="app/src/main/res/drawable/bloom_launcher_pose.png" alt="Bloom's pink sprout mascot" width="120">
</p>

Bloom is a calm Android period tracker for young teens: track a period, understand cycle patterns, and learn with supportive illustrated lessons.

[Download APK](https://github.com/Bloooommmm/Bloom-Frontend/releases/latest) | [Backend](https://github.com/Bloooommmm/Bloom-Backend) | [Organization](https://github.com/Bloooommmm)

## Features

- Google sign-in with Firebase Authentication
- Today view with cycle context and period logging
- Calendar history with period start and end updates
- Illustrated Learn articles
- Profile photo, display name, email, and sign out
- Bloom mascot, brand colors, and light theme

## Stack

- Kotlin 2.2.10
- Jetpack Compose and Material 3
- Android Gradle Plugin 9.2.1
- Gradle 9.4.1
- Minimum SDK 27, target SDK 36
- Compile SDK 37, JDK 21
- Firebase Authentication

The app uses `https://bloom-be.yanzz.dev` as its production API base URL.

## Run locally

1. Open this repository in Android Studio with JDK 21 and Android SDK 37 installed.
2. Download `google-services.json` for Firebase Android package `id.ac.binus.bloomapp`; place it in `app/`.
3. Enable Google sign-in in Firebase Authentication and register your debug/release certificate SHA-1.
4. Let Gradle sync finish, then run the `app` configuration on an emulator or device.

```powershell
.\gradlew.bat test
.\gradlew.bat assembleDebug
```

Firebase configuration is supplied locally through `app/google-services.json` and through the `GOOGLE_SERVICES_JSON` GitHub Actions secret. Do not commit that file.

## Version

Current app version: `1.0.0` (`versionCode` 2).

## APK pipeline

`.github/workflows/android-build-release.yml` follows the KarFlex release flow:

- Pushes to `main`, pull requests, and manual runs test and upload debug APKs.
- A tag such as `v1.0.0` must match `versionName`. It also builds a signed release and publishes a GitHub Release.
- Both APKs and `android-sha1.txt` are attached to the release. Actions artifacts remain available for 30 days.
- The signed APK uses the existing keystore. CI rejects a certificate that differs from `ANDROID_UPLOAD_CERT_SHA1`.

Repository secrets: `GOOGLE_SERVICES_JSON`, `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`, and `ANDROID_UPLOAD_CERT_SHA1`.

For the next release, update `versionName`, increment `versionCode`, commit, then push a matching `vMAJOR.MINOR.PATCH` tag. Install the signed APK for the stable Firebase certificate; runner-generated debug certificates differ.

## Current boundaries

The UI is English. Learn has five articles with four illustrations each and no audio. The cycle ring currently uses a 28-day estimate, not a personalized prediction. Mood input, reminders, and a theme switch are not yet exposed in the app; mood and AI endpoints are available in the backend.
