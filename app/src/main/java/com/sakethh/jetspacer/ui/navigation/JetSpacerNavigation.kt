package com.sakethh.jetspacer.ui.navigation

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
sealed interface JetSpacerNavigation {
    @Serializable
    sealed interface Root : JetSpacerNavigation {
        @Serializable
        data object Latest : Root

        @Serializable
        data object Explore : Root

        @Serializable
        data object Headlines : Root

        @Serializable
        data object Collections : Root

    }

    @Serializable
    sealed interface Latest : JetSpacerNavigation {
        @Serializable
        data object Settings : Latest
    }

    @Serializable
    sealed interface Explore : JetSpacerNavigation {

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
    sealed interface Headlines : JetSpacerNavigation {
        @Serializable
        data class TopHeadlineDetailScreenRoute(
            val encodedString: String
        )
    }
}