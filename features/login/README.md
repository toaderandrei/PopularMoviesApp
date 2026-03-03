# :features:login

Authentication feature module. Handles TMDb account login, guest mode, welcome screen with animated backdrop, and account profile management.

## Key Classes
- `LoginViewModel` - TMDb authentication flow
- `WelcomeViewModel` / `WelcomeRoute` - Welcome screen with login/guest options
- `AccountViewModel` / `AccountScreen` - Account profile and logout

## Navigation Routes
- `welcome` - Welcome screen (auth start destination)
- `login` - TMDb login screen
- `account` - Account profile screen (top bar action)

## Dependencies
- `core:domain`, `core:models`, `core:ui`, `core:resources`, `core:common`
