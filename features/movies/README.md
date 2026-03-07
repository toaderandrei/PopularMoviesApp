# :features:movies

Movie browsing feature module. Displays popular, top-rated, upcoming, and now-playing movies in horizontal category rows with drill-down to category lists and movie details.

## Key Classes
- `MoviesViewModel` / `MoviesUiState` - Main movie list state management
- `MovieDetailsViewModel` / `MovieDetailsScreen` - Movie detail screen with cast, videos, reviews
- `MovieCategoryViewModel` / `MovieCategoryRoute` - Full category list browsing

## Navigation Routes
- `movies` - Main movies screen (bottom nav tab)
- `movies/details/{movieId}` - Movie details
- `movies/category/{categoryType}` - Category list

## Dependencies
- `core:domain`, `core:models`, `core:ui`, `core:resources`, `core:common`, `core:analytics`
