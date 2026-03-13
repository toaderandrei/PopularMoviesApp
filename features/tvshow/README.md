# :features:tvshow

TV series browsing feature module. Displays popular, top-rated, airing-today, and on-the-air TV shows with category lists and detail screens.

## Key Classes
- `TvShowViewModel` / `TvShowUiState` - Main TV show list state management
- `TvShowDetailsViewModel` / `TvShowDetails` - TV show detail screen
- `TvShowCategoryViewModel` / `TvShowCategoryRoute` - Full category list browsing

## Navigation Routes
- `tvshow` - Main TV shows screen (bottom nav tab)
- `tvshow/details/{tvShowId}` - TV show details
- `tvshow/category/{categoryType}` - Category list

## Dependencies
- `core:domain`, `core:models`, `core:ui`, `core:resources`, `core:shared`, `core:analytics`
