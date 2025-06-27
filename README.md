# 🚗 Car Animation with HERE SDK (Explore Edition)

This Android project demonstrates how to animate a car moving along a route using the **HERE SDK for Android (Explore Edition)**.

It features:

- Smooth car movement animation along a polyline
- Route drawing with custom styling
- Integration of HERE MapView (not MapViewLite)
- Modular and clean code structure

---

## 🛠️ Setup Instructions

### 1. Clone the Repository

```bash
git clone git@github.com:rusyanto/car-animation.git
cd car-animation
```

### 2. Add Required Files

#### 🔐 `local.properties`

Create a file named `local.properties` in the **project root directory**, and insert your HERE SDK credentials:

```ini
here.accessKeyId=YOUR_ACCESS_KEY_ID
here.accessKeySecret=YOUR_ACCESS_KEY_SECRET
```
> ⚠️ **Do not commit this file** — it is excluded via `.gitignore`.

You can obtain your credentials from the [HERE Platform Portal](https://platform.here.com/).

---

#### 📦 HERE SDK `.aar` File

This project depends on the HERE SDK `.aar` file, which **cannot be redistributed publicly** due to licensing restrictions.

**To add it:**

1. Log in to [HERE Platform Portal](https://platform.here.com/) and download the Explore Edition `.aar` (e.g. `heresdk-explore-4.22.5.0.aar`)
2. Place the file into this directory:
```
app/libs/
```
3. Rename it (optional) to:
```
heresdk-explore.aar
```
4. Make sure your `build.gradle.kts` includes:
```kotlin
implementation(mapOf("name" to "heresdk-explore", "ext" to "aar"))
```

---

## ✅ Build & Run

1. Open the project in **Android Studio (Arctic Fox or newer)**
2. Let Gradle sync
3. Run the app on a device or emulator (ensure internet access)

If the map does not appear, check:

* Your HERE credentials are valid
* Emulator/device has network access
* `.aar` is correctly placed and referenced

---

## 📁 Project Structure Overview

```
car-animation/
├── app/
│   ├── libs/                      # <-- HERE SDK .aar file goes here
│   └── src/
├── local.properties               # <-- Add Access Key ID and Secret here
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## ⚠️ Notes

* This app simulates a car moving along a static polyline route.
* It uses the RoutingEngine to calculate a route between two predefined locations.
* Designed to work entirely with HERE MapView (no MapViewLite).

---

## 🔐 Security & Best Practices

* The `.aar` and `local.properties` are **not committed** to version control.
* If you're sharing this repo publicly, verify `.gitignore` includes:

```
/local.properties
/app/libs/*.aar
```

---

## 📬 Contact

For any questions or suggestions, feel free to open an issue or contact [@rusyanto](https://github.com/rusyanto).

---

## 📃 License

This project is provided for demo and educational purposes only and is not affiliated with or endorsed by HERE Technologies.

---
