# Growlauncher

Modern Android launcher app built with Kotlin + Jetpack Compose.

## Features
- Firebase Authentication flow with SharedPreferences session persistence
- Main launcher dashboard with Growtopia launcher integration (`grow://`)
- Script manager (add/toggle/delete scripts)
- Settings and real-time theme picker (dark/light + purple/blue/green)
- MVVM architecture with Hilt dependency injection

## Structure
```
app/src/main/java/com/ching791/growlauncher/
├── ui/screens
├── ui/components
├── ui/theme
├── data/preferences
├── data/repositories
├── data/models
├── viewmodel
├── utils
└── di
```

## Setup
1. Add your Firebase `google-services.json` into `app/`.
2. Sync in Android Studio Iguana+.
3. Build and run on Android 7.0+ (API 24).

## Notes
- Growtopia launch target package: `com.rtsoft.growtopia`
- If Growtopia is missing, the app shows a user-friendly message instead of crashing.
