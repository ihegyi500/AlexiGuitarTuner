# Repository Guide

## Active Bug-Fix Plan

- Read [requirements.md](requirements.md) and [tasks.md](tasks.md) before implementing the analyzed bug fixes. These documents describe planned work; unchecked tasks do not claim completed changes or successful validation.
- Follow the task dependencies and mark a task `- [x]` only after its implementation and required verification are complete. Record actual results and keep unavailable device checks open.
- [COMPOSE_MIGRATION.md](COMPOSE_MIGRATION.md) records the completed UI migration. Its backend-preservation statements describe that historical scope; explicit bug fixes may change ViewModels, repositories, services, and the database as required by the current plan.
- Preserve user data with an explicit tested Room migration. Previously collapsed duplicate strings and unstored custom string order cannot be reconstructed with certainty; do not invent replacement user data.
- Keep application code independent of Compose below the UI layer, preserve notification navigation compatibility, and avoid unrelated dependency upgrades or architecture rewrites.
- The repository-root `AGENTS.md` remains the discovery entry point and points to this detailed guide. Keep documentation links valid after reorganizing files.

## Project
AlexiGuitarTuner is a single-module Android application written in Kotlin. Features include a guitar tuner, metronome, chord table, and string gauge calculator, plus user settings. A single `ComponentActivity` hosts Jetpack Compose and Material 3; Navigation Compose provides five bottom navigation destinations. Dependencies include Hilt, Room, Kotlin coroutines/Flow, and a local TarsosDSP Android JAR. There are no fragment screens, View Binding, RecyclerView adapters, or XML layouts.

## Layout
- `app/src/main/java/com/ihegyi/alexiguitartuner/`: application source.
- `commons/`: shared database/DAOs, entities, settings, dependency injection, and UI.
- `feat_tuner/`, `feat_metronome/`, `feat_chordtable/`, `feat_sgc/`: feature packages under the application source root, organized into `data`, `domain`, `di`, and `presentation` layers.
- Each `presentation/ui/` directory contains Compose route functions that collect existing ViewModel state and screen functions with state/callback parameters for testing.
- `commons/presentation/ui/`: shared theme, selectors, tuning dialogs, settings, and the `AlexiApp` scaffold/navigation host.
- `MainActivity.kt`: Compose entry point and translation of metronome notification intents into Compose navigation requests; the splash is composable content.
- `app/src/main/res/`: strings, launcher/window themes, vector drawables, and other non-layout resources.
- `res/navigation/bottom_nav.xml`: minimal activity destinations required by the unchanged metronome service's `NavDeepLinkBuilder`. This resource is notification compatibility metadata, not the UI navigation implementation. Preserve its IDs and intent handling unless that service is explicitly migrated.
- `app/src/test/`: local unit tests, including ViewModel tests and fake repositories.
- `app/src/androidTest/`: Compose tests for all five pages, Hilt end-to-end navigation/tuning/notification tests, and Room DAO tests. `compose/` contains screen interaction tests; `NavigationTest.kt` uses the real activity and a separate database copied from the bundled asset.
- `app/libs/TarsosDSP-Android.jar`: local audio dependency; preserve it.

## Build And Validation
Use the checked-in Gradle wrapper from the repository root. The project uses Gradle 9.3.1, Android Gradle Plugin 9.1.1 with built-in Kotlin 2.2.10, the matching Kotlin Compose compiler plugin, KSP 2.3.12, and Java 17 compatibility. Compose BOM 2026.09.00 supplies Compose 1.12.1 and Material 3 1.4.0. Compile SDK is 37.0; target/minimum SDK remain 35/28. Hilt 2.60.1 and Room 2.8.5 support this toolchain. Do not apply the separate Kotlin Android plugin alongside AGP's built-in Kotlin. Use JDK 17 and an installed Android SDK; keep machine-specific SDK paths in `local.properties`.

Windows PowerShell commands:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:lintDebug
.\gradlew.bat :app:connectedDebugAndroidTest
```

The connected test task requires an awake, unlocked emulator or device running API 28 or later. Set `ANDROID_SERIAL` to select a device when several are attached. Screen tests use Compose's v2 JUnit4 rules; keep tests synchronized with state rather than arbitrary sleeps. To run a focused local test:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.ihegyi.alexiguitartuner.feat_tuner.presentation.viewmodel.TunerViewModelTest"
```

Run checks appropriate to the change and report any environment or dependency failures accurately. Do not claim checks passed unless executed successfully.

To run only the Compose page tests:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.package=com.ihegyi.alexiguitartuner.compose
```

On Windows, stop older Gradle daemons if lint reports locked migrated JARs. Do not disable lint to bypass that environment issue. See `COMPOSE_MIGRATION.md` for the migration plan, compatibility rationale, and verification record.

## Implementation Conventions
- Follow nearby Kotlin formatting and naming; use four-space indentation and preserve existing package boundaries.
- Keep repository interfaces in `domain`, implementations in `data`, Hilt wiring in `di`, and fragments/ViewModels/UI state in `presentation`.
- Follow existing constructor injection and dispatcher injection patterns. Collect ViewModel Flows with `collectAsStateWithLifecycle`; scope UI coroutine work and audio cleanup to the current lifecycle/composition.
- Keep ViewModels, repositories, services, and the database independent of Compose. Route functions adapt their existing APIs; screen functions accept state and callbacks. Some existing ViewModels mutate nested lists without emitting a new Flow value; keep any required snapshot/recomposition adaptation in the UI rather than silently changing backend behavior during UI work.
- Build UI with Material 3 composables, lazy lists, Compose Canvas, and Compose dialogs. Do not add fragment UI, XML layouts, View Binding, or AndroidView wrappers for migrated screens.
- Use Android string resources for user-visible text, accessible labels for controls/drawings, stable semantic tags for UI tests, and saveable state for form/navigation state. Support small and landscape windows through constrained, scrollable content.
- For persistence changes, review Room entities, DAOs, database configuration, and affected device tests together; preserve user data.
- Extend relevant existing tests and fakes for behavior changes. Test screen interactions through Compose semantics and keep activity end-to-end coverage for bottom navigation, recreation, tuning persistence, and notification routing. Use separate test databases; never clear a user's database for UI test setup. Keep the existing JUnit, coroutine test, Turbine, Robolectric, and Hilt infrastructure where applicable.
- For audio changes, check microphone permission handling, lifecycle cleanup, and stopping recording/playback; automated checks may need device verification.

## Change Hygiene
Keep changes focused on the requested task. Avoid unrelated dependency upgrades or broad formatting changes. Do not edit generated outputs in `build/`, `app/build/`, `.gradle/`, or `.kotlin/`. Do not commit local SDK paths, credentials, or machine-specific IDE settings. Preserve existing uncommitted work.
