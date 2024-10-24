package com.sakethh.jetspacer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sakethh.jetspacer.news.presentation.TopHeadlineDetailScreen
import com.sakethh.jetspacer.news.presentation.TopHeadlinesScreen
import com.sakethh.jetspacer.ui.theme.JetSpacerTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            JetSpacerTheme {
                Surface {
                    NavHost(
                        navController = navController,
                        startDestination = TopHeadlinesScreen
                    ) {
                        composable<TopHeadlinesScreen> {
                            TopHeadlinesScreen(navController)
                        }
                        composable<TopHeadlineDetailScreen> { navBackStackEntry ->
                            TopHeadlineDetailScreen(
                                navBackStackEntry.toRoute<TopHeadlineDetailScreen>().rawArticle
                            )
                        }
                    }
                }
            }
        }
    }
}


@Serializable
object TopHeadlinesScreen

@Serializable
data class TopHeadlineDetailScreen(
    val rawArticle: String
)

