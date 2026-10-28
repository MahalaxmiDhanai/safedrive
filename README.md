# WakeDrive 🚗

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![Kotlin](https://img.shields.io/badge/kotlin-1.8.0-blue.svg?logo=kotlin)](http://kotlinlang.org)

A real-time Android application that uses machine learning and computer vision to detect driver drowsiness and prevent accidents. The app monitors driver alertness through facial analysis and provides multi-level audio-visual alerts when signs of fatigue are detected.

> **Based on [AlertDrive](https://github.com/Roxonn-FutureTech/AlertDrive) by [Roxonn-FutureTech](https://github.com/Roxonn-FutureTech)**, licensed under Apache 2.0.

## Features 🌟

- **Real-time Face Detection**: Continuous monitoring of driver's face using ML Kit
- **Multi-level Drowsiness Detection**:
  - Eye openness tracking
  - Head position monitoring
  - Blink rate analysis
- **Smart Alert System**:
  - WARNING: Notification sound with brief vibration
  - SEVERE: Continuous alarm with extended vibration
  - CRITICAL: Intensive alarm with pulsing vibration pattern
- **Session Analytics**: Track and analyze driving patterns
- **Privacy-Focused**: All processing done locally on device
- **Battery Efficient**: Optimized for minimal power consumption

## Screenshots 📱

[Add screenshots of your app here]

## Requirements 📋

- Android SDK 24 or higher
- Android Studio Arctic Fox or newer
- Kotlin 1.8.0 or higher
- Device with front-facing camera

## Installation 🔧

1. Clone the repository:
```bash
git clone https://github.com/MahalaxmiDhanai/wakedrive.git
```

2. Open the project in Android Studio

3. Build and run the app:
```bash
./gradlew assembleDebug
```

## Architecture 🏗

The app follows MVVM (Model-View-ViewModel) architecture and uses modern Android development practices:

- **UI Layer**: Jetpack Compose
- **Business Logic**: ViewModel + Coroutines
- **ML Processing**: ML Kit Face Detection
- **Camera**: CameraX API
- **Concurrency**: Kotlin Coroutines + Flow

## Dependencies 📚

- AndroidX Core KTX
- Jetpack Compose
- CameraX
- ML Kit Face Detection
- Kotlin Coroutines
- Android Lifecycle Components

## Contributing 🤝

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Privacy Policy 🔒

This app does not collect or store any personal data. All face detection and drowsiness analysis is performed locally on the device. No images or data are transmitted to external servers.

## License 📄

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments 👏

- Based on [AlertDrive](https://github.com/Roxonn-FutureTech/AlertDrive) by Roxonn-FutureTech (Apache 2.0)
- ML Kit for Face Detection
- Android CameraX team

## Contact 📧

Mahalaxmi Dhanai — [GitHub](https://github.com/MahalaxmiDhanai)

Project Link: [https://github.com/MahalaxmiDhanai/wakedrive](https://github.com/MahalaxmiDhanai/wakedrive)
