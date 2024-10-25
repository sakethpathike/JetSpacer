package com.sakethh.jetspacer.common.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sakethh.jetspacer.explore.presentation.ExploreScreenViewModel

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentNavRoute = navController.currentBackStackEntryAsState().value?.destination
    AnimatedVisibility(visible = navItems.map { it.route }.any {
        currentNavRoute?.hasRoute(it::class) ?: false
    } && ExploreScreenViewModel.isSearchBarExpanded.value.not(), enter = fadeIn()) {
        NavigationBar {
            navItems.forEach {
                val isSelected = currentNavRoute?.hasRoute(it.route::class) ?: false
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(it.route)
                        }
                    },
                    icon = {
                        Icon(if (isSelected) it.selectedIcon else it.nonSelectedIcon, null)
                    },
                    label = {
                        Text(
                            text = it.name,
                            style = if (isSelected) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall
                        )
                    })
            }
        }
    }
}

data class BottomNavigationItem<T>(
    val name: String,
    val route: T,
    val selectedIcon: ImageVector,
    val nonSelectedIcon: ImageVector
)

private val navItems = listOf(
    BottomNavigationItem(
        name = "Home",
        route = HomeScreenRoute,
        selectedIcon = Icons.Filled.Home,
        nonSelectedIcon = Icons.Outlined.Home
    ),
    BottomNavigationItem(
        name = "Explore",
        route = ExploreScreenRoute,
        selectedIcon = Icons.Filled.Explore,
        nonSelectedIcon = Icons.Outlined.Explore
    ),
    BottomNavigationItem(
        name = "Headlines",
        route = TopHeadlinesScreenRoute,
        selectedIcon = Icons.Filled.Newspaper,
        nonSelectedIcon = Icons.Outlined.Newspaper
    ),
    BottomNavigationItem(
        name = "Collection",
        route = TopHeadlinesScreenRoute,
        selectedIcon = Icons.Filled.Collections,
        nonSelectedIcon = Icons.Outlined.Collections
    ),
)