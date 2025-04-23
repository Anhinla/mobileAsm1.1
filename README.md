# Android 3D Interaction Camera App

This is a mobile application developed using **Android Studio**. The app demonstrates the use of various Android UI components along with **CameraX** to capture images, and a **joystick** controller to interact with a 3D object rendered in the app.

---

## Features

- **User Interface**
  - Built using:
    - `ConstraintLayout`
    - `RelativeLayout`
    - `HorizontalScrollView`
    - `Button`
    - `ImageView`
- **Camera Functionality**
  - Utilizes [CameraX API](https://developer.android.com/media/camera/camerax?hl=vi) to:
    - Open the device camera
    - Display camera preview
    - Capture images and save to device
- **3D Object Interaction**
  - Utilize [SceneView](https://github.com/SceneView/sceneview-android) to easily incorporate 3D and AR capabilities 
  - Includes a [Joystick controller](https://www.instructables.com/A-Simple-Android-UI-Joystick/)
  - Allows real-time interaction with a 3D object (e.g., rotation, movement)

---

## Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Android SDK 21+
- Device or emulator with camera access (for CameraX)

### Clone the Repository

```bash
git clone https://github.com/Anhinla/mobileAsm1.1.git
```

### Build the Project
1. Open the project in Android Studio.
2. Let Gradle sync and resolve all dependencies.
3. Connect a physical Android device or start an emulator.
4. Click Run (▶️) to build and deploy the app.
