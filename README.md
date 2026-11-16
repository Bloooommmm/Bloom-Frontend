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

The companion API lives in [`../Bloom-Backend`](../Bloom-Backend). Keep API URLs environment-specific when integration is added.
