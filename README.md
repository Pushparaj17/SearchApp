Character Search (Android)
======================================
Goal
----
Build a simple Android app (Kotlin + Jetpack Compose) that lets users search characters
using the public Rick and Morty API and view character details.
API
---
Search endpoint:
  https://rickandmortyapi.com/api/character/?name=<query>
Detail endpoint:
  https://rickandmortyapi.com/api/character/<id>
  
User Features
----------------------------------
1) Search
   - Search bar at the top.
   - Results update after each keystroke.
   - Requests are made to the Rick & Morty API using the typed query.
   - Shows a progress indicator while searching (UI remains responsive).
2) Results grid
   - Characters are displayed in a grid (image + name).
   - Tapping an item opens a detail screen.
3) Detail view
   - Title showing the character name.
   - Full-width character image.
   - Text showing: species, status, origin.
   - Type is shown only when present.
   - Created date is displayed in a formatted (human-readable) form.
4) Tests
   - Unit test for SearchViewModel (MockK).
   - Compose UI test for SearchScreen (Compose test rule).
     
Tech Stack
----------
- Language: Kotlin
- UI: Jetpack Compose (Material 3)
- Navigation: Navigation Compose
- DI: Dagger-Hilt
- Async: Kotlin Coroutines + Flow
- Networking: Retrofit + OkHttp + Moshi
- Images: Coil
- Testing:
  - Unit tests: JUnit4 + MockK + kotlinx-coroutines-test
  - UI tests: androidx.compose.ui.test
    
Architecture (MVVM)
--------------------------
This project uses MVVM with a small domain layer (use cases):
- UI (Compose screens)
  - SearchScreen: renders SearchUiState and forwards user actions to ViewModel.
  - CharacterDetailScreen: renders CharacterDetailUiState.
- ViewModels (MVVM)
  - SearchViewModel:
    - Holds StateFlow<SearchUiState>.
    - onQueryChange updates query immediately so TextField always shows typed text.
    - Cancels the previous search job and collects the latest Flow from the use case.
  - CharacterDetailViewModel:
    - Reads the "id" navigation argument using SavedStateHandle.
    - Collects the detail use case Flow into a StateFlow<CharacterDetailUiState>.
- Use cases (domain/usecase)
  - SearchCharactersUseCase(query): Flow<Resource<List<Character>>>
  - GetCharacterUseCase(id): Flow<Resource<Character>>
  Resource is a small wrapper around Loading/Success/Error for easy UI state mapping.
- Repository
  - SearchRepository calls the API and maps DTOs to the domain model.
  - Contains a small in-memory cache by id to avoid re-fetching details when possible.
  - Treats HTTP 404 for search as "no results" (the API uses 404 for empty matches).
- Dependency Injection (Hilt)
  - AppModule provides OkHttp/Moshi/Retrofit/RickMortyApi/Repository as singletons.
  - SearchApp is @HiltAndroidApp.
  - MainActivity is @AndroidEntryPoint.

How to Run
----------
Prerequisites:
- Android Studio (or command line) with Android SDK installed.
Steps (Android Studio):
1) Open the project.
2) Sync Gradle.
3) Run the 'app' configuration on an emulator/device.
Steps (Command line):
1) Ensure ANDROID_HOME / ANDROID_SDK_ROOT is set OR create local.properties with:
     sdk.dir=/path/to/Android/Sdk
2) Build:
     ./gradlew assembleDebug
3) Install/run via Android Studio or adb.
   
How to Run Tests
----------------
Unit tests (JVM):
  ./gradlew testDebugUnitTest
Instrumented UI tests (requires emulator/device):
  ./gradlew connectedDebugAndroidTest
