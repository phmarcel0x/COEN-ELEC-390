# MEDIMAPS: An Android Medication Tracker App
### COEN/ELEC-390: Concordia University Mini Capstone Project
### Team Members: Marcelo, Kateryna, Yassine, Michel, Matthew

## Tech Stack

### Mobile Application

- **Programming Language:** Java
  - Developed in Android Studio for creating the main functionality of the Android application.

### Microcontroller

- [**ESP32 Board: KeeYees ESP32 Development Board**](https://www.espressif.com/sites/default/files/documentation/esp32-wroom-32_datasheet_en.pdf)
  - Equipped with Wi-Fi and Bluetooth, used as the foundation for the GPS module.
  - **Programming:** C/C++ in the Arduino IDE for hardware programming and data uploading.
  - **Features:**
    - Integrated Wi-Fi and Bluetooth
    - Low power consumption
    - Dual-core processor for high performance

- [**GPS Module: HiLetgo GY-NEO6MV2 GPS Module**](https://www.datasheethub.com/wp-content/uploads/2022/08/NEO6MV2-GPS-Module-Datasheet.pdf)
  - Provides precise location tracking, connected to the ESP32 board.

### Mapping and Geolocation

- **Google Maps SDK**
  - Enables mapping and geolocation features in the app.
  - **Capabilities:**
    - Dynamic maps
    - Real-time location tracking
    - Geocoding services

### Data Management and Storage

- **Firebase Realtime Database**
  - Cloud-hosted database for real-time data synchronization.
  - Scalable and flexible for data storage and real-time syncing.

- **Firebase Authentication**
  - Robust identity and access management system.
  - Supports multiple authentication methods for enhanced security.
  - Integrates with Firebase Database for secure data handling.
