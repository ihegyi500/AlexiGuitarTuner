# AlexiGuitarTuner

Android guitar tuner, metronome, chord table, and string gauge calculator.

## Build and run

The UI uses Jetpack Compose and Material 3 in a single `ComponentActivity`, with Navigation Compose for the five bottom navigation pages. Existing ViewModels, repositories, services, and Room entities remain unchanged.

Use JDK 17, Android SDK platform 37.0, and the checked-in Gradle 9.3.1 wrapper (AGP 9.1.1). Compose BOM 2026.09.00 supplies Compose 1.12.1 and Material 3 1.4.0. The minimum/target SDK are still 28/35. Configure the SDK location in your untracked `local.properties` file. Set `JAVA_HOME` to the JDK directory containing `bin`, rather than to `bin` itself. Restart existing terminals after changing the environment variable. In Android Studio, select JDK 17 as the Gradle JDK.

From PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug
```

Start an API 28 or newer emulator in Android Studio Device Manager, or connect a device with USB debugging enabled. Select that device and run the `app` configuration in Android Studio. Alternatively, with the SDK platform-tools directory on PATH:

```powershell
adb devices
.\gradlew.bat :app:installDebug
adb shell am start -W -n com.ihegyi.alexiguitartuner/.MainActivity
```

Tap Allow microphone on the tuner page to enable pitch detection; reference tones also work without microphone access. The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

Instrumented tests require an awake, unlocked device or emulator. They include two tests for each feature/settings page, three activity end-to-end tests (bottom navigation/recreation, tuning persistence, and notification navigation), and the existing 23 DAO tests. End-to-end tests use a separate database copied from the bundled asset:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

Set `ANDROID_SERIAL` to select one device when several are connected. Test reports are under `app/build/reports/tests/testDebugUnitTest/` and `app/build/reports/androidTests/connected/debug/`.

The small `res/navigation/bottom_nav.xml` resource remains solely for the unchanged metronome service notification's `NavDeepLinkBuilder`. It contains activity targets, not fragment UI; `MainActivity` translates its intent to the Compose metronome route. See `COMPOSE_MIGRATION.md` for the replacement map and toolchain rationale.
