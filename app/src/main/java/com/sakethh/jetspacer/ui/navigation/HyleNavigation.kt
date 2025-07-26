package com.sakethh.jetspacer.ui.navigation

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
sealed interface HyleNavigation {
    @Serializable
    sealed interface Root : HyleNavigation {
        @Serializable
        data object Latest : Root

        @Serializable
        data object Explore : Root

        @Serializable
        data object Collections : Root

        @Serializable
        data object Settings : Root

    }

    @Serializable
    sealed interface Latest : HyleNavigation {
        @Serializable
        data object Settings : Latest
    }

    @Serializable
    sealed interface Explore : HyleNavigation {

        @Serializable
        data object APODArchiveScreen


        @Serializable
        data object MarsGalleryRoute

        @Serializable
        data class SearchResultScreenRoute(
            val encodedString: String
        )
    }

    @Serializable
    sealed interface Headlines : HyleNavigation {
        @Serializable
        data class TopHeadlineDetailScreenRoute(
            val encodedString: String
        )
    }
}