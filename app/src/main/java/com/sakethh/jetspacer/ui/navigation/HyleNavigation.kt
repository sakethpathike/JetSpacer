package com.sakethh.jetspacer.ui.navigation

import android.annotation.SuppressLint
import com.sakethh.jetspacer.domain.model.rover_latest_images.LatestPhoto
import com.sakethh.jetspacer.ui.screens.home.state.apod.ModifiedAPODDTO
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
sealed interface HyleNavigation {
    @Serializable
    sealed interface Root : HyleNavigation {
        @Serializable
        data object Home : Root

        @Serializable
        data object Explore : Root

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
        data object MarsGalleryScreen

        @Serializable
        data class SearchResultScreen(
            val encodedString: String
        )

    }

    @Serializable
    sealed interface APODArchiveScreen {
        @Serializable
        data class APODDetailScreen(val apod: String)
    }
    @Serializable
    sealed interface MarsGalleryScreen {
        @Serializable
        data class RoverImageDetailsScreen(val image: String)
    }

    @Serializable
    sealed interface Headlines : HyleNavigation {
        @Serializable
        data class TopHeadlineDetailScreen(
            val encodedString: String
        )
    }
}