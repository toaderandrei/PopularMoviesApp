# Architecture Diagrams

This directory contains architecture diagrams for the Popular Movies KMP application.

## PlantUML Diagrams

We use PlantUML for our architecture diagrams to keep them version-controlled and easy to update.

### Available Diagrams

1. **`kmp_architecture.puml`** - Complete KMP architecture diagram showing:
   - Platform layer (Android/iOS apps)
   - Shared UI layer
   - Feature modules
   - Core modules (domain, data, network, database, etc.)
   - Data flow and dependencies
   - Platform-specific implementations

2. **`module_dependencies.puml`** - Module dependency graph showing:
   - All project modules
   - Dependencies between modules
   - Layer separation (app → features → core)
   - KMP structure

### Viewing the Diagrams

#### Option 1: Online PlantUML Server
1. Copy the contents of the `.puml` file
2. Go to [PlantUML Web Server](http://www.plantuml.com/plantuml/uml/)
3. Paste the content and view the rendered diagram

#### Option 2: VS Code Extension
1. Install the [PlantUML extension](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml)
2. Open the `.puml` file
3. Press `Alt+D` to preview

#### Option 3: IntelliJ/Android Studio Plugin
1. Install the [PlantUML Integration plugin](https://plugins.jetbrains.com/plugin/7017-plantuml-integration)
2. Open the `.puml` file
3. The diagram will render automatically in the preview pane

#### Option 4: Generate PNG Images
Using PlantUML command line:

```bash
# Install PlantUML (macOS)
brew install plantuml

# Generate PNG from PlantUML file
plantuml kmp_architecture.puml
plantuml module_dependencies.puml

# This will create .png files in the same directory
```

### Updating the Diagrams

To update a diagram:
1. Edit the `.puml` file
2. Regenerate the image (if needed)
3. Commit both the source `.puml` file and generated image

### PlantUML Resources

- [PlantUML Official Documentation](https://plantuml.com/)
- [Component Diagram Syntax](https://plantuml.com/component-diagram)
- [Package Diagram Syntax](https://plantuml.com/package)
- [PlantUML Cheat Sheet](https://ogom.github.io/draw_uml/plantuml/)

## Screenshots

- `popular_movies_1.jpg` - App screenshots
- `popular_movies_2.jpg` - App screenshots
- `popular_movies_3.jpg` - App screenshots
- `ui_app.png` - UI overview

## Legacy Diagrams

- `clean_architecture_diagram.png` - **DEPRECATED** - Old Android-only architecture diagram (Activity/Fragment based). Replaced by `kmp_architecture.puml`.
