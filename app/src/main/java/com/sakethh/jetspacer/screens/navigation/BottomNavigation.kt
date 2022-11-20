package com.sakethh.jetspacer.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sakethh.jetspacer.ui.theme.AppTheme
import com.sakethh.jetspacer.R


data class BottomNavigationItem(
    val name: String,
    val navigationRoute: String,
    val selectedIcon: ImageVector?,
    val nonSelectedIcon: ImageVector?,
    val selectedIconRes: Int? = null,
    val nonSelectedIconRes: Int? = null
)

@Composable
fun BottomNavigationComposable(
    navController: NavController
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val bottomNavDataList: List<BottomNavigationItem> = listOf(
        BottomNavigationItem(
            name = "Home",
            navigationRoute = NavigationRoutes.HOME_SCREEN,
            selectedIcon = Icons.Rounded.Home,
            nonSelectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            name = "Space",
            navigationRoute = NavigationRoutes.SPACE_SCREEN,
            selectedIcon = null,
            nonSelectedIcon = null,
            selectedIconRes = R.drawable.satellite_rounded,
            nonSelectedIconRes = R.drawable.satellite_outlined
        ),
        BottomNavigationItem(
            name = "Bookmarks",
            navigationRoute = NavigationRoutes.BOOKMARKS_SCREEN,
            selectedIcon = Icons.Rounded.Bookmarks,
            nonSelectedIcon = Icons.Outlined.Bookmarks
        ),
    )
    AppTheme {
        NavigationBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
            bottomNavDataList.forEach {
                val isSelected = currentDestination.toString() == it.navigationRoute
                val currentIconVector = if (isSelected) {
                    it.selectedIcon
                } else {
                    it.nonSelectedIcon
                }
                val currentIconRes = if (isSelected) {
                    R.drawable.satellite_rounded
                } else {
                    R.drawable.satellite_outlined
                }
                NavigationBarItem(selected = isSelected, onClick = {
                    if (!isSelected) {
                        navController.navigate(it.navigationRoute)
                    }
                }, icon = {
                    if (currentIconVector != null) {
                        Icon(
                            imageVector = currentIconVector,
                            contentDescription = it.name,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = currentIconRes),
                            contentDescription = it.name,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                })
            }
        }
    }
}