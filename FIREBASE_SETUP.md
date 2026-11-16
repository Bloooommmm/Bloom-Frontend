# Firebase setup for Bloom Mobile

Firebase is not connected to the repository yet because the Firebase project ID, `google-services.json`, and signing fingerprints belong to the project owner. Do the following in Firebase Console before enabling Google sign-in or Firebase services.

## 1. Create the Android app

1. Open [Firebase Console](https://console.firebase.google.com/) and create or select the Bloom project.
2. Add an Android app with package name `id.ac.binus.bloomapp`.
3. Download `google-services.json`.
4. Put it at `Bloom-Mobile/app/google-services.json` locally. This file is ignored by Git.

## 2. Add signing fingerprints

From `Bloom-Mobile`:

```powershell
.\gradlew.bat signingReport
```

Copy the SHA-1 and SHA-256 fingerprints for the debug variant into Firebase Console. Add the release/upload-key fingerprints too before testing a release APK. Debug and release certificates are different.

## 3. Enable Google Authentication

1. In Firebase Console, open **Authentication → Sign-in method**.
2. Enable **Google**.
3. Set the support email.
4. Add the Android app's SHA fingerprints first; Google sign-in will fail without them.

The current login button is still a UI preview. Full Google sign-in requires adding the Google/Firebase Android dependencies, applying the Google Services Gradle plugin, and changing the button to obtain a Firebase ID token.

When the Firebase project is ready, the Gradle wiring is:

```kotlin
// Bloom-Mobile/build.gradle.kts
plugins {
    id("com.google.gms.google-services") version "4.5.0" apply false
}

// Bloom-Mobile/app/build.gradle.kts
plugins {
    id("com.google.gms.google-services")
}
```

Use the Firebase BoM for Firebase libraries (for example `firebase-auth`) and declare the library without a separate version. The `google-services.json` package name must match `id.ac.binus.bloomapp`.

## 4. Connect Firebase Auth to the backend

The backend currently has no authentication middleware or token endpoint. The production flow should be:

```text
Android Firebase Google sign-in
-> Firebase ID token
-> Authorization: Bearer <id-token>
-> Bloom backend Firebase Admin verification
-> user upsert and protected API routes
```

Before enabling this in production, add Firebase Admin credentials to the backend deployment secret store. Never commit a service-account JSON file, Firebase private key, or ID token.

## 5. Optional Firebase App Distribution

The included GitHub Actions workflow builds and uploads a debug APK as an artifact. To distribute builds through Firebase App Distribution, add a separate workflow with:

- `FIREBASE_APP_ID`: Android Firebase app ID (`1:...:android:...`)
- `FIREBASE_SERVICE_ACCOUNT_JSON`: a GitHub Actions secret containing the Firebase service-account JSON
- `FIREBASE_TESTER_GROUPS`: comma-separated tester groups

Keep App Distribution credentials in GitHub Actions secrets. Do not put them in `google-services.json` committed to source or in the APK workflow YAML.

## 6. Backend URL

The mobile API client is configured with the production base URL:

```text
https://bloom-be.yanzz.dev
```

The app currently uses the public lessons endpoint at `GET /api/lessons`. Confirm the domain resolves and that `/health` returns HTTP 200 before testing on a device.
