package com.sakethh.jetspacer.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.screens.explore.ExploreScreenViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomNavigationBar() {
    val navController = LocalNavController.current
    val currentNavRoute = navController.currentBackStackEntryAsState().value?.destination
    val navItems = remember {
        listOf(
            BottomNavigationItem(
                name = "Home",
                route = HyleNavigation.Root.Home,
                selectedIcon = Icons.Filled.Settings,
                selectedDrawable = R.drawable.outline_planet_24,
                nonSelectedIcon = Icons.Outlined.CameraAlt
            ),
            BottomNavigationItem(
                name = "Explore",
                route = HyleNavigation.Root.Explore,
                selectedIcon = Icons.Filled.Explore,
                nonSelectedIcon = Icons.Outlined.Explore
            ),
            BottomNavigationItem(
                name = "Collection",
                route = HyleNavigation.Root.Collections,
                selectedIcon = Icons.Filled.Collections,
                nonSelectedIcon = Icons.Outlined.Collections
            ),
            BottomNavigationItem(
                name = "Settings",
                route = HyleNavigation.Root.Settings,
                selectedIcon = Icons.Filled.Settings,
                nonSelectedIcon = Icons.Outlined.Settings
            ),
        )
    }
    AnimatedVisibility(
        visible = navItems.map { it.route }.any {
        currentNavRoute?.hasRoute(it::class) == true
    } && ExploreScreenViewModel.isSearchBarExpanded.value.not(),
        enter = slideInVertically { it },
        exit = slideOutVertically { it }) {
        NavigationBar {
            navItems.forEach {
                val isSelected = currentNavRoute?.hasRoute(it.route::class) == true
                NavigationBarItem(selected = isSelected, onClick = {
                    if (!isSelected) {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }, icon = {
                    if (it.selectedDrawable != null) {
                        Icon(
                            painter = painterResource(it.selectedDrawable),
                            contentDescription = null
                        )
                    } else {
                        Icon(if (isSelected) it.selectedIcon else it.nonSelectedIcon, null)
                    }
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
    val name: String,
    val route: T,
    val selectedIcon: ImageVector,
    val selectedDrawable: Int? = null,
    val nonSelectedIcon: ImageVector
)