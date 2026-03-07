# :core:ui

Shared UI components and navigation infrastructure for the Android app layer.

## Key Classes
- `MainScreenDestination` - Navigation destination definitions and route constants
- `CoilInitializer` - Coil image loading initialization with TMDb interceptor
- `TmdbImageCoilInterceptor` - OkHttp interceptor for TMDb image URL resolution
- Shared composable components used across feature modules

## Dependencies
- `core:models`, `core:common`, `core:resources`
