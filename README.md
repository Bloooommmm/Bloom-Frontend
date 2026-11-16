# Bloom Mobile

Bloom Mobile is the Android client for Bloom, a menstrual-cycle companion that combines cycle education, daily cycle context, and supportive health guidance.

## Current scope

- Onboarding screen
- Login screen with a temporary Google sign-in simulation
- Today screen with a 28-day cycle preview and phase indicator
- Calendar screen
- Placeholder Lessons and Profile destinations
- Shared Bloom theme, typography, icons, and Compose components

The current mobile flow is UI-first. Login, cycle calculations, lessons, profile data, and AI insights are not connected to the backend yet.

## Stack

- Kotlin
- Jetpack Compose and Material 3
- Android Gradle Plugin 9.2.1
- Kotlin 2.2.10
- Gradle 9.4.1
- Minimum Android SDK 27, target SDK 36

## Open in Android Studio

1. Open the `Bloom-Mobile` directory in Android Studio.
2. Allow Gradle sync to finish.
3. Select the `app` configuration.
4. Run it on an Android emulator or a connected device.

PowerShell build commands:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat test
```

The companion API is configured at `https://bloom-be.yanzz.dev`. The Lessons tab loads published lessons from `GET /api/lessons` and shows a retry state when the API is unavailable.

## APK pipeline

GitHub Actions builds a debug APK on pushes and pull requests for `main`. Download it from the workflow's **Artifacts** section. The workflow runs unit tests first and retains each APK artifact for 14 days.

