# Novix

<div align="center">
  <!-- Other Badges -->
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=flat&logo=android"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-blue?style=flat&logo=kotlin"/>
  <img src="https://img.shields.io/badge/Architecture-Clean%20Architecture-orange?style=flat"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-green?style=flat&logo=jetpackcompose"/>
  <br><br>

</div>

  <!-- [Download APK Badge](https://www.mediafire.com/file/7eodqd8mzimqbc1/app.apk/file) -->

Dive into your favorite movies and let us suggest the most popular similar titles you’ll love. Save your personal list and enjoy a magical browsing experience with a stunning design!

Novix is a mix of two words: `Nova` – a star that suddenly increases in brightness `Flix` from *"flicks"*  a casual term for movies and shows.

<table align="center">
  <tr>
    <th>Dark</th>
    <th>Light</th>
  </tr>
  <tr>
    <td>
      <img width="400" src="https://github.com/user-attachments/assets/5f71af45-1433-4ce1-9b67-ce69148cc10f" />
    </td>
    <td>
      <img width="400" src="https://github.com/user-attachments/assets/666986be-87c9-4506-a036-a57a8ec3cc6c" />
    </td>
  </tr>
</table>

   ----
## Setup
### 1. Clone Repository
```bash
git clone https://github.com/Baghdad-Squad/Novix
cd Novix
```

### 2. Add API Configuration
Get your API key from [TMDb API](https://www.themoviedb.org/settings/api), then create `secrets.properties` in root directory:
```properties
API_KEY="your_api_key_here"
BASE_URL="https://api.themoviedb.org/3/"
ACCESS_TOKEN="your_token_here"
```

### 3. Firebase Setup
- Create project at [Firebase Console](https://console.firebase.google.com/) with package name `com.baghdad.novix`.
- Add `google-services.json` to `app/` folder
- Enable *Analytics*, *Crashlytics*, *Performance*

### 4. Build & Run
```bash
./gradlew build
./gradlew installDebug
```
#  Novix  Overview
## Architecture
<img width="1483" height="580" alt="by_component" src="https://github.com/user-attachments/assets/80bb7f75-40e8-4eed-83c0-4a1c38a53831" />

## Module Architecture Summary

| Module                  | Description |
|--------------------------|-------------|
| **App**                 | Main application module that wires everything together with DI and app-level configs. |
| **Design System**       | Centralized UI foundation providing reusable components, themes and typography. |
| **UI**                  | Contains Compose screens, navigation destinations. |
| **ViewModel**           | Manages presentation logic, UI state, and user interactions following the MVVM pattern. |
| **Domain**              | Core business logic with use cases and repository contracts. |
| **Entity**              | Pure data models (data classes, enums) that are framework independent. |
| **Remote DataSource**   | Handles network operations with API services, DTOs, Retrofit setup, and error handling. |
| **Local DataSource**    | Manages local persistence via Room database, DAOs, entities, and storage error handling. |
| **Repository**          | Implements repository pattern to unify local/remote sources. |
| **Islamic Image Loader**| Specialized image loader with AI-powered sensitive content detection and smart blurring. |


## Technology Stack

| Technology | Description |
|------------|-------------|
| **Kotlin** | Primary programming language |
| **Jetpack Compose** | Declarative Android UI framework |
| **MVVM** | Presentation architecture pattern |
| **Koin → Hilt & Dagger** | Migration to official DI solution |
| **Navigation 2** | Smooth and reliable screen navigation |
| **Paging 3** | Effortless list pagination |
| **Coil** | Fast, lightweight image loading |
| **Ktor → Retrofit & OKHttp** | Migration to stable networking stack |
| **Room** | Local caching and storage |
| **Datastore** | Local key-value storage |
| **Firebase Crashlytics** | Real-time crash reporting |
| **Firebase Analytics** | User behavior tracking |
| **Firebase Performance** | App performance monitoring |
| **Kotlinx Coroutines** | Asynchronous programming support |
| **JUnit** | Unit testing framework |
| **MockK** | Mocking library for Kotlin |
| **Google Truth** | Fluent assertion framework |
| **Turbine** | Testing Kotlin Flow emissions |

---

## Demo
[![Watch the Quick Tour on YouTube](https://img.youtube.com/vi/8bWvNDF2fPs/0.jpg)](https://www.youtube.com/watch?v=8bWvNDF2fPs)

---

##  Contributors
<a href="https://github.com/Baghdad-Squad/Novix/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Baghdad-Squad/Novix" />
</a>

Special thanks to our mentors for the guidance and support throughout this journey:
- [Ahmed Nasser](https://github.com/ahmednasserzaza)
- [Amnah](https://github.com/amnah44)
- [Bareq](https://github.com/iBareq)
