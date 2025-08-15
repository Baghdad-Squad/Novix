# Novix

### Dive into your favorite movies and let us suggest the most popular similar titles you’ll love. Save your personal list and enjoy a magical browsing experience with a stunning design!

>### Novix is a mix of two words: `Nova` – a star that suddenly increases in brightness `Flix` from *"flicks"*  a casual term for movies and shows.

-  <img width="1000" height="472" alt="Image" src="https://github.com/user-attachments/assets/5f71af45-1433-4ce1-9b67-ce69148cc10f" />

- <img width="1000" height="473" alt="Image" src="https://github.com/user-attachments/assets/666986be-87c9-4506-a036-a57a8ec3cc6c" />

   ----
## Why Novix ?
In our humble little project, we’ve sprinkled a few special touches just for you, little things to make your experience extra delightful!

- ### Use Component Modularization
  -  **Reusability** : Use the same component in different projects or features.

  - **Faster builds** : Only rebuild the part that changed, not the whole project.

  - **Better code organization** : Structured by features or functionality.

  - **Parallel development** : Developers work on different parts without conflicts.

  - **Easier testing** : Test each module independently.

  - **Better dependency control** : Each part only gets the libraries it needs.

  - **Maintainability & scalability** : Easily add or remove features.

  - **Dynamic delivery support** : Load certain parts only when needed.

- ###  Use Firebase

  - **Firebase App Performance** : Monitor app performance in real time, track slow screens, and detect bottlenecks.

  - **Firebase Analytics** : Track user behavior, events, and engagement to make data-driven decisions.

  - **Firebase Crashlytics** : Get real-time crash reports with stack traces to quickly fix issues.

  - #### Steps to integrate:

  - Add Firebase SDK to the project `google-services.json for Android`.

  - Enable the desired services in Firebase Console.

  - Initialize Firebase in our app code.

  - Add tracking *for Analytics* or custom logging *for Crashlytics*.

- ### CI/CD Setup

  - #### *CI* Build Verification

    Pipeline ensures the project compiles successfully before merging into develop.

  - ####  *CI*  Test Coverage

    Pipeline checks unit test coverage, must exceed 80%.

    Merge into develop is blocked if coverage is below threshold.

  - #### *CD* Deployment

    Pipeline generates APK or App Bundle after a PR is approved and merged into develop.

    Automatically publishes the build to Firebase App Distribution.

- #### Islamic Image Viewer

  * a sleek **Coil** based module with one simple Image composable, using on-device AI to gently blur content sensitive to Islamic culture for a safe and smooth experience.

## Application Build

- ###  Use Navigation 2
  - We use **Navigation 2** to make moving between screens simple and reliable. It helps us manage complex navigation flows, handle back actions correctly, and keep your experience smooth without glitches.

- ### TMDb API
  * Our app is powered by The Movie Database **TMDb API**, bringing you *up to date* movie data, trailers, and more all thanks to [TMDb’s developer resources](https://developer.themoviedb.org/reference/intro/getting-started)


- ###  [Use Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
  Effortless pagination for smooth scrolling and big lists

- ### [coil](https://coil-kt.github.io/coil/)
  Fast and lightweight image loading for Android because pictures matter

---
> **"The only constant in life is change."** — *Heraclitus*

- ### From Koin to Hilt

  **As Android developers**, we started with *Koin*, and it was an excellent experience: **simple**, **fast**, and easy to understand, which helped us get work done efficiently. However, *Hilt*, **Google’s official library** built on **Dagger**, is designed specifically for Android, offering full support for all modules and greater flexibility for larger projects.

  **For developers** who plan to work on **Kotlin Multiplatform projects**, *Koin* is the practical choice, as it works in shared modules like **domain** and **entity**. *On the other hand*, for projects targeting Android only, *Hilt* is preferable, providing cleaner code, better performance, and seamless integration with Dagger when needed.

  **As a squad**, we decided to migrate the Novix from *Koin* to *Hilt and Dagger* for Android specific modules, while still appreciating the significant role Koin played in making our initial experience smooth and productive.

- ### From Ktor to Retrofit & OKHttp
  **As Android developers**, we started with *Ktor*, and it was a **smooth** and **flexible choice**, helping us handle networking efficiently across. *However*, to achieve industry standard reliability and support, we transitioned to *Retrofit and OKHttp*, the most trusted libraries in Android development.

  **For Kotlin Multiplatform projects**, Ktor remains a great option for shared modules like **domain** and **entity**. But for Android-focused projects, *Retrofit and OKHttp* provide **unmatched stability**, **advanced features**, and **seamless integration**, ensuring our apps run smoothly and scale effortlessly.

  **As a squad**, we refactored Novix Remote Data Source to Retrofit, keeping all changes contained within that module, while fully recognizing the crucial role Ktor played in kickstarting our project efficiently.

#  Novix  Overview
### Technologies & Usage

| Tech | Usage |
|------|-------|
| [Kotlin](https://kotlinlang.org/) | Programming Language |
| [Jetpack Compose](https://developer.android.com/jetpack/compose) | Declarative UI Framework |
| [MVVM](https://proandroiddev.com/mvi-architecture-pattern-for-android-apps-5c7e6f4a7c6a) | Architecture Pattern |
| [Koin](https://insert-koin.io/) | Dependency Injection |
| [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) | Dependency Injection |
| [Dagger](https://dagger.dev/) | Dependency Injection |
| [Ktor](https://ktor.io/) | Integrating with Backend |
| [Retrofit](https://square.github.io/retrofit/) | Integrating with Backend |
| [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) | Pagination |
| [Room](https://developer.android.com/training/data-storage/room) | Caching and Local Storage |

## Architecture
<img width="1483" height="580" alt="by_component" src="https://github.com/user-attachments/assets/80bb7f75-40e8-4eed-83c0-4a1c38a53831" />

#  Module Architecture

Novix follows a clean, modular architecture that promotes separation of concerns, testability, and scalability. Each module has a specific responsibility and clear boundaries.

## App Module
The main application module that orchestrates everything together. It contains:
- Application class and dependency injection setup
- Main activity and navigation host
- App-level configurations and themes
- Depends on all other modules to wire everything up

##  Design System Module
Our centralized UI foundation that ensures consistency across the app:
- **Reusable UI components** (buttons, cards, dialogs)
- **Color palette and typography** definitions
- **Custom themes** and styling
- **Animation utilities** and transitions
- Used by UI module to maintain visual consistency

##  UI Module
Contains all the screen compositions and UI logic:
- **Jetpack Compose screens** for each feature
- **Navigation destinations** and routing
- **UI-specific utilities** and extensions
- Depends on: `design_system`, `viewmodel`

##  ViewModel Module
Handles presentation logic and state management:
- **Screen-specific ViewModels** following MVVM pattern
- **UI state management** and user interactions
- **Business logic coordination** between UI and domain
- Depends on: `domain`, `entity`

## Domain Module
The heart of our business logic - completely framework independent:
- **Use cases** that encapsulate business rules
- **Business logic** and application-specific operations
- **Repository interfaces** (contracts only, no implementations)
- **Domain models** and business entities
- Depends on: `entity`

##  Entity Module
Pure data models shared across all layers:
- **Data classes** representing core business objects
- **Enum classes** for constants and states
- **Interfaces** for data contracts
- **No dependencies** - completely independent module

##  Remote DataSource Module
Handles all network communications:
- **API service interfaces** and implementations
- **Network DTOs** and response models
- **Retrofit configurations** and interceptors
- **Error handling** for network operations
- Depends on: `entity`

##  Local DataSource Module
Manages local data persistence:
- **Room database** setup and configurations
- **DAO interfaces** for database operations
- **Local DTOs** and database entities
- **Error handling** for Storage

- Depends on: `entity`

##  Repository Module
Implements the repository pattern as a single source of truth:
- **Repository implementations** that coordinate data sources
- **Data mapping** between different data representations
- **Caching strategies** and offline-first approach
- **Data synchronization** logic between local and remote
- Depends on: `domain`, `entity`, `local_datasource`, `remote_datasource`

##  Islamic Image Loader Module
A specialized module for culturally-sensitive image handling:
- **AI-powered content detection** for Islamic guidelines
- **Smart image blurring** for sensitive content
- **Coil-based image loading** with custom transformations
- **Configurable sensitivity levels**
- Used by: `design_system`, `ui`

---
##  Download Latest Release

- [Quick Tour](https://github.com/Baghdad-Squad/Novix)

- [![Download APK](https://img.shields.io/badge/Download-APK-brightgreen?style=for-the-badge&logo=android)]( )

---

## Setup

- [Android Studio](https://developer.android.com/studio)

- [JDK 17+](https://adoptium.net/temurin/releases/)

- Android SDK 34


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
AUTHORIZATION_TOKEN="your_token_here"
```

### 3. Firebase Setup (Optional)
- Create project at [Firebase Console](https://console.firebase.google.com/)
- Add `google-services.json` to `app/` folder
- Enable *Analytics*, *Crashlytics*, *Performance*

### 4. Build & Run
```bash
./gradlew build
./gradlew installDebug
```

## Common Issues
- **Build fails**: Check `secrets.properties` exists in root
- **Crashes**: Verify API keys and internet connection
- **Sync fails**: Clean project and invalidate caches

##  Contributors
Special thanks to our mentor for the guidance and support throughout this journey.
[Ahmed Nasser](https://github.com/ahmednasserzaza) & [Amnah](https://github.com/amnah44)
Thanks to the amazing Baghdad Squad team who made this possible
[Abdulaziz](https://github.com/abdulazizacc) , [Aboud](https://github.com/ABDULLAHHG),[Edrees](https://github.com/MuhammedEdrees) , [Fares](https://github.com/FaresM0hamed), [Fara7](https://github.com/Farah315), [Fatmah](https://github.com/fatmahgazy), [Karrar](https://github.com/karrar-abbas), [Malak](https://github.com/Malak187), [Mahmoud](https://github.com/MahmoodTarek), [Omer](https://github.com/omer1998), [Zinah](https://github.com/Arzo-zi)