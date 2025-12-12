# Quote Explorer App

An inspiring Android application built with Kotlin and Jetpack Compose that allows users to explore, share, and save their favorite quotes.

## Features

- **Explore Quotes**: Infinite scrolling list of inspirational quotes fetched from [Quotable API](https://github.com/lukePevey/quotable).
- **Favorites**: Save quotes to your personal collection (stored locally).
- **Beautiful Design**: Modern UI with rounded cards, warm yellow accents, and smooth animations.
- **Optimistic UI**: Instant interaction feedback for favoriting quotes.
- **Offline Support**: Favorites are available even when offline.
- **Sharing**: Easily copy or share quotes to other apps.

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit + OkHttp + Gson
- **Local Storage**: Room Database
- **Dependency Injection**: Manual DI (scalable to Hilt/Koin)
- **Asynchronous**: Coroutines + Flow
- **Image Loading**: Coil (ready for hero images)

## Setup & Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   ```

2. **Open in Android Studio**
   - Open Android Studio and select "Open an existing Android Studio Project".
   - Navigate to the `QuoteExplorer` directory.

3. **Build & Run**
   - Wait for Gradle sync to complete.
   - Select an emulator or connected physical device.
   - Click the **Run** button (green play icon).

## Project Structure

```
com.quoteexplorer
├── data
│   ├── api        # Retrofit API definitions
│   ├── local      # Room Database & Entities
│   └── repository # Data repository
├── model          # Data classes
├── ui
│   ├── components # Reusable Compose components (QuoteCard, etc.)
│   ├── navigation # Navigation graph
│   ├── screens    # Screen-level composables (Home, Details, Favorites)
│   └── theme      # Design system (Color, Type, Shape, Theme)
└── QuoteExplorerApp.kt # Application class
```

## API

The app uses the free [Quotable API](https://api.quotable.io). No API key is required.

## Screenshots

*(Screenshots would be placed here)*

## License

This project is open source.
