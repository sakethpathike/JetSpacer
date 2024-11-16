## JetSpacer

An Android client that merges multiple space-related APIs into a single, simple application. Built
with Jetpack Compose and Kotlin, based on a mixture of Clean Architecture and MVVM.

JetSpacer doesn't strictly follow Clean Architecture. Initially, I wanted to rewrite it using Clean
Architecture, but it felt over-engineered in a few scenarios, so I've used a typical MVVM style in
those cases.

Custom pagination implementation is used in the `APOD Archive`, `Mars Gallery`, and `Headlines` to
prevent pulling everything at once.

JetSpacer allows you to search through APOD, Mars Rover Photos, EPIC, and the NASA Image Library. By
default, an API key is provided, but you can use your own, which you can obtain here after signing
up: https://api.nasa.gov/#signUp.

The bookmarking feature is available for APOD and Mars Rover Photos, and caching is implemented for
`Headlines`—you can clear the cache and local database at any time.

### Screenshots

| ![Image 1](assets/1.png) | ![Image 2](assets/2.png)   | ![Image 3](assets/3.png)   | ![Image 4](assets/4.png)   |
|--------------------------|----------------------------|----------------------------|----------------------------|
| ![Image 5](assets/5.png) | ![Image 6](assets/6.png)   | ![Image 7](assets/7.png)   | ![Image 8](assets/8.png)   |
| ![Image 9](assets/9.png) | ![Image 10](assets/10.png) | ![Image 11](assets/11.png) | ![Image 12](assets/12.png) |

---

### License

This project is licensed under the MIT License.