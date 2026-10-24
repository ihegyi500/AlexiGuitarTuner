# Compose migration

## Scope and replacement map

ViewModels, UI state models, domain, data, DI, services, and database remain unchanged. A single ComponentActivity hosts Compose; Android still requires an Activity entry point.

| Existing UI | Replacement |
| --- | --- |
| MainActivity binding and bottom_nav_menu.xml | Compose scaffold and Navigation Compose destinations |
| SplashActivity and activity_splash.xml | Compose splash in the single activity |
| feat_tuner/presentation/ui/TunerFragment | TunerScreen with runtime permission and lifecycle effects |
| feat_metronome/presentation/ui/MetronomeFragment and RhythmView | MetronomeScreen and accessible beat buttons |
| feat_chordtable/presentation/ui/ChordTableFragment and ChordTableView | ChordTableScreen and Compose Canvas fretboard |
| feat_sgc/presentation/ui/SGCFragment and SGCInfoDialog | SGCScreen and Compose AlertDialog |
| commons/presentation/ui/UserSettingsFragment | UserSettingsScreen |
| CreateTuningDialog, SelectTuningDialog, DeleteTuningDialog | Compose tuning dialogs |
| PitchListAdapter, StringListAdapter | Compose lazy lists and editable rows |
| All res/layout and res/layout-land files | Responsive Compose layouts with scrolling |

## Implementation order

1. Record current behavior and backend fingerprints; verify latest published stable Compose and compatible toolchain.
2. Add Compose BOM/compiler, Material 3, lifecycle, navigation, and UI test dependencies. Keep View Binding and legacy UI while testing the replacement.
3. Implement shared theme, selectors, settings and tuning dialogs using existing ViewModel APIs.
4. Implement tuner, metronome, chord table, and string gauge calculator under their presentation/ui packages.
5. Replace the activity host with Compose splash and five bottom navigation destinations.
6. Add page interaction tests, dialog tests, and bottom navigation end-to-end tests. Run unit, UI, database/device tests and lint. Fix UI/build failures without backend edits.
7. Only after tests pass, delete legacy fragments/dialogs/views/adapters, both layout directories, and XML menu; remove obsolete UI dependencies. Retain a minimal navigation resource for the unchanged metronome service notification's NavDeepLinkBuilder, and translate its intent in MainActivity into a Compose destination.
8. Repeat validation after cleanup, check backend fingerprints, and update AGENTS.md and README.md.

## Validation

Use deterministic screen state/callback tests for UI behavior and an activity test with Hilt and the real database asset for bottom navigation. Keep existing DAO and ViewModel tests. Exercise permission denial without requiring microphone recording in deterministic screen tests. Device tests require API 28 or newer.

## Toolchain

The published stable BOM `2026.09.00` maps Compose UI/runtime/foundation to `1.12.1` and Material 3 to `1.4.0`. Its AAR metadata requires compile SDK 37 and AGP 9.1 or later. The build therefore uses AGP 9.1.1, Gradle 9.3.1, built-in Kotlin 2.2.10 with its matching Compose compiler, KSP 2.3.12, and Hilt 2.60.1. Room runtime/compiler move together to 2.8.5 because the original 2.6.1 processor fails with the new KSP toolchain (`unexpected jvm signature V`). No entities, queries, schema version, repository, or ViewModel source is changed by this migration. The target/minimum SDK remain 35/28.

References: [Compose releases](https://developer.android.com/jetpack/androidx/releases/compose), [Compose BOM](https://developer.android.com/develop/ui/compose/bom), [Hilt releases](https://github.com/google/dagger/releases).

## Legacy removal gate

Before removing the old UI, all 39 local unit tests, lint, and all 36 instrumented tests passed on an isolated API 35 emulator. The instrumented suite consists of 23 existing DAO tests, 10 page interaction tests, and 3 activity end-to-end tests. Only then were the 14 legacy Kotlin UI/adapter files and all 17 portrait/landscape layout XML files removed. View Binding, Fragment/View UI dependencies, and obsolete widget styles were also removed. The notification navigation resource now contains two activity targets and no fragment destinations. Backend source fingerprints match the start of the migration.

After cleanup, `:app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:connectedDebugAndroidTest` passed again: 39 unit tests and 36 instrumented tests, with zero failures or skips. Lint reports zero errors; nonblocking dependency, resource, and existing backend warnings remain. The debug APK also installs and cold-launches successfully on the API 35 emulator.
