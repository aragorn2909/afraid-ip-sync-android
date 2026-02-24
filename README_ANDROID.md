# Afraid IP Sync - Android Companion App

This is the native Android companion app for the [Afraid IP Sync](https://github.com/[YOUR_USERNAME]/external-ip-checker) Docker container.

## Features
- **Real-time Monitoring**: Connect to your backend to see domain sync status.
- **Manual Sync**: Trigger a sync check directly from your phone.
- **Material 3 Design**: Modern, responsive UI with dark mode support.

## How to Build

### 1. Requirements
- **Android Studio** (Hedgehog or newer recommended).
- Internet connection to sync Gradle dependencies.

### 2. Import Project
1. Open Android Studio.
2. Select **File > Open**.
3. Navigate to the `afraid-ip-sync-android` directory and click **OK**.
4. Android Studio will start syncing Gradle. This may take a few minutes.

### 3. Build & Run
1. Connect your Android device or start an emulator.
2. Click the **Run** (Green Play) button in the toolbar.
3. The app will launch on your device.

### 4. Configuration
1. On the first launch, enter the URL of your Afraid IP Sync backend (e.g., `http://[YOUR_SERVER_IP]:7777`).
2. Ensure your phone can reach this address (VPN or local Wi-Fi).
3. The app will fetch the status of your monitored domains!

## Technical Notes
- Built with **Jetpack Compose**.
- Networking handled by **Retrofit**.
- Managed by a **ViewModel** with Coroutines Flow.
