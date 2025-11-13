![Build](https://github.com/Al-Taie/composeArchitecturePlugin/workflows/Build/badge.svg)

<!-- Plugin description -->
# Compose Architecture Plugin

The Compose Architecture Plugin is a powerful tool that enhances the development of new Jetpack Compose features by providing pre-defined templates. These templates enable developers to accelerate their workflow when creating new Jetpack Compose UI Architecture.

## New Feature Dialog

The **New Feature Dialog** is a convenient feature of the plugin that simplifies the creation of various files related to Jetpack Compose UI Architecture. Once you provide a name for your feature, the plugin will automatically generate the following files and directories:

- `presentation/features/[featName]` - Package (lowercase) (optional)
  - `[FeatName]UiState` - This file contains the UIState, which serves as the main state holder for the screen.
  - `[FeatName]Screen` - An empty screen file with a preview, allowing you to quickly visualize the initial layout.
  - `[FeatName]Effect` - This file defines the effects specific to your screen.
  - `[FeatName]Action` - A file responsible for controlling the UI logic and interactions of the screen.
  - `[FeatName]ViewModel` - A basic implementation of the viewModel for your feature.

In addition to the feature-specific files, the plugin also generates the following files as a foundation for your project:

- `[base]BaseViewModel` - A basic implementation of the base viewModel with helper functions, providing a starting point for your feature's view models.
- `[base]ErrorState` - A basic data class designed to hold error state information.
- `[utils][extensions]eventListener` - An extension function that simplifies listening to UI events.

With the Compose Architecture Plugin, you can streamline your development process and focus on building robust Jetpack Compose features without having to manually create boilerplate code.

<!-- Plugin description end -->
