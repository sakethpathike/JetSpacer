# JetSpacer

JetSpacer is an android client that merges multiple space-related APIs including NASA's APOD and Mars Rover's API into a single, simple application with a good UI.

- As this project is meant as a "client," you can change API keys within the app.

## Tech Stack

- Kotlin - The app is completely written in Kotlin
- Jetpack Compose - Jetpack Compose is used to write the UI, making it a completely Jetpack Compose-based project
- Material 3 - Used for building the UI with existing components provided by the library
- Ktor-client - Used to make HTTP requests to the respective APIs
- Room - Used for local database
- Kotlin Coroutines - Used for managing background tasks
- Kotlin Flows - Used for handling asynchronous data streams
- Kotlinx Serialization - Used for deserializing API responses.
- Coil - Used for loading images and Architecture Components such as DataStore, Navigation, ViewModel are also used to make this app alive!

- #### "JetSpacer" uses the MVVM architecture concept to keep code organised and separate business logic from the UI.

## Screenshots

| Home Screen | Space Screen | News Screen |
| -------- | -------- | -------- |
| ![Screenshot_2023_02_02_16_59_09_74_a723baeefcc773b465b5b84fb7477d54](https://user-images.githubusercontent.com/83284398/216790711-ba1dfa0d-8a2b-444b-82fa-80e3e750cbcc.jpg) |![Screenshot_2023_02_02_16_58_08_73_a723baeefcc773b465b5b84fb7477d54](https://user-images.githubusercontent.com/83284398/216790732-26b70b88-1030-4833-beda-d9f8c59e1775.jpg)| ![Screenshot_2023_02_03_23_09_27_08_a723baeefcc773b465b5b84fb7477d54](https://user-images.githubusercontent.com/83284398/216790742-33f23173-8a78-4804-ac6b-6e0982d2795a.jpg)|

| Bookmarks Screen |
| -------- |
| ![Screenshot_2023_02_02_17_11_47_18_a723baeefcc773b465b5b84fb7477d54](https://user-images.githubusercontent.com/83284398/216790808-dc0136a8-29be-4e0f-ac65-e1a287e1850b.jpg)|

| Other Screens |
| -------- |
| ![Screenshot_2023_02_02_17_00_09_58_a723baeefcc773b465b5b84fb7477d54](https://user-images.githubusercontent.com/83284398/216790868-b453d052-6fd5-4963-aeec-78454e8953a2.jpg)|
|![Screenshot_2023_02_02_17_02_03_54_a723baeefcc773b465b5b84fb7477d54](https://user-images.githubusercontent.com/83284398/216790877-281af70e-9e73-4cd3-a46a-937a0b8d5df2.jpg) |
|![Screenshot_2023_02_02_17_02_14_97_a723baeefcc773b465b5b84fb7477d54](https://user-images.githubusercontent.com/83284398/216790882-b1a3d530-9879-4d4b-842d-f1ce196f6605.jpg) |
|![Screenshot_2023_02_02_17_03_21_48_a723baeefcc773b465b5b84fb7477d54](https://user-images.githubusercontent.com/83284398/216790891-98c0c640-9938-4639-a08b-0de320b24984.jpg)|
|![Screenshot_2023_02_02_22_12_50_95_a723baeefcc773b465b5b84fb7477d54](https://user-images.githubusercontent.com/83284398/216790898-d31f4b46-de33-45c6-b8f4-f2728b305233.jpg)|

## GitHub Releases
The latest stable release of JetSpacer is available on [GitHub Releases](https://github.com/sakethpathike/JetSpacer/releases/tag/v1.0.0). You can download the APK file from there; or [click here](https://github.com/sakethpathike/JetSpacer/releases/download/v1.0.0/JetSpacer_v1.0.0.apk)

## Known Issues
- Certain UI elements are not optimized for different screen sizes
- The UI implementation of the NASA video APOD Card requires improvement

## Upcoming Features 
- Option to manually select the theme, instead of referring to system settings by default
- Improved user interface for better user experience
- Optimization of UI elements for different screen sizes
- Search and sorting options in bookmarks
- Caching wherever possible

#### Note: This project is in development and new features and improvements will be added over time.
