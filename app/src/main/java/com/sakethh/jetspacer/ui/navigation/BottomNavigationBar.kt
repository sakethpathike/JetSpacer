package com.sakethh.jetspacer.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.screens.explore.ExploreScreenViewModel

@Composable
fun BottomNavigationBar() {
    val navController = LocalNavController.current
    val currentNavRoute = navController.currentBackStackEntryAsState().value?.destination
    val navItems = remember {
        listOf(
            BottomNavigationItem(
                name = "Latest",
                route = JetSpacerNavigation.Root.Latest,
                selectedIcon = Icons.Filled.CameraAlt,
                nonSelectedIcon = Icons.Outlined.CameraAlt
            ),
            BottomNavigationItem(
                name = "Explore",
                route = JetSpacerNavigation.Root.Explore,
                selectedIcon = Icons.Filled.Explore,
                nonSelectedIcon = Icons.Outlined.Explore
            ),
            BottomNavigationItem(
                name = "Headlines",
                route = JetSpacerNavigation.Root.Headlines,
                selectedIcon = Icons.Filled.Newspaper,
                nonSelectedIcon = Icons.Outlined.Newspaper
            ),
            BottomNavigationItem(
                name = "Collection",
                route = JetSpacerNavigation.Root.Collections,
                selectedIcon = Icons.Filled.Collections,
                nonSelectedIcon = Icons.Outlined.Collections
            ),
        )
    }
    AnimatedVisibility(visible = navItems.map { it.route }.any {
        currentNavRoute?.hasRoute(it::class) == true
    } && ExploreScreenViewModel.isSearchBarExpanded.value.not(), enter = fadeIn()) {
        NavigationBar {
            navItems.forEach {
                val isSelected = currentNavRoute?.hasRoute(it.route::class) == true
                NavigationBarItem(selected = isSelected, onClick = {
                    if (!isSelected) {
                        navController.navigate(it.route)
                    }
                }, icon = {
                    Icon(if (isSelected) it.selectedIcon else it.nonSelectedIcon, null)
                }, label = {
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
    val name: String, val route: T, val selectedIcon: ImageVector, val nonSelectedIcon: ImageVector
)