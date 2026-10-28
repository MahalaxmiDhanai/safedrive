# WakeDrive 🚗💤

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=for-the-badge&logo=android" />
  <img src="https://img.shields.io/badge/Language-Kotlin-blue?style=for-the-badge&logo=kotlin" />
  <img src="https://img.shields.io/badge/License-Apache%202.0-orange?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Min%20SDK-24-red?style=for-the-badge" />
</p>

<p align="center">
  <b>Stay awake. Stay alive.</b><br/>
  WakeDrive is a real-time Android app that uses AI and your phone's front camera to detect driver drowsiness and alert you before it's too late.
</p>

---

## What It Does

WakeDrive silently monitors your face while you drive. The moment it detects signs of fatigue — drooping eyes, head nodding, reduced blink rate — it fires an alert to wake you up.

No internet needed. No data uploaded. Everything runs on your device.

---

## Features ✨

- 🎥 **Real-time face monitoring** using ML Kit Face Detection
- 👁️ **Eye openness tracking** — detects when eyes start closing
- 🔄 **Head movement analysis** — catches nodding and tilting
- 📊 **Blink rate monitoring** — abnormal blink patterns trigger alerts
- 🔔 **3-level alert system**:
  - **WARNING** — gentle notification + short vibration
  - **SEVERE** — loud alarm + long vibration
  - **CRITICAL** — continuous alarm + pulsing vibration
- 📈 **Session statistics** — review your alertness data after each drive
- 🔒 **100% private** — no camera data ever leaves your phone
- 🔋 **Battery optimized** — lightweight processing, minimal drain

---

## Tech Stack 🛠️

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM |
| Camera | CameraX |
| AI / ML | ML Kit Face Detection |
| Database | Room |
| Async | Kotlin Coroutines + Flow |
| Storage | DataStore Preferences |
| Min SDK | Android 7.0 (API 24) |

---

## Getting Started 🚀

### Prerequisites
- Android Studio Hedgehog or newer
- Android device with a front-facing camera
- Android 7.0 (API 24) or higher

### Installation

1. Clone the repository
```bash
git clone https://github.com/MahalaxmiDhanai/safedrive.git
```

2. Open in Android Studio

3. Let Gradle sync automatically

4. Connect your Android device and click **Run**

### Build from terminal
```bash
./gradlew assembleDebug
```

---

## How It Works 🧠

```
Camera Feed → ML Kit Face Detection → Drowsiness Analyzer
     ↓
Eye Openness + Head Rotation + Blink Rate
     ↓
Drowsiness Score (0.0 → 1.0)
     ↓
NORMAL / WARNING / SEVERE / CRITICAL
     ↓
Sound Alert + Vibration + Notification
```

---

## Permissions Required

| Permission | Reason |
|---|---|
| `CAMERA` | Front camera access for face detection |
| `VIBRATE` | Vibration alerts |
| `WAKE_LOCK` | Keep screen on while monitoring |
| `RECORD_AUDIO` | Audio output for alarms |

---

## Screenshots 📱

> Coming soon — run the app and add your own!

---

## Author 👩‍💻

**Mahalaxmi Dhanai**

- GitHub: [@MahalaxmiDhanai](https://github.com/MahalaxmiDhanai)

---

## License 📄

Copyright 2026 Mahalaxmi Dhanai

Licensed under the Apache License, Version 2.0.
See the [LICENSE](LICENSE) file for full details.
