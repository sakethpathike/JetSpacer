package com.sakethh.jetspacer.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
val bottomNavDataList: List<BottomNavigationItem> = listOf(
    BottomNavigationItem(
        name = "Home",
        navigationRoute = NavigationRoutes.HOME_SCREEN,
        selectedIcon = Icons.Filled.Home,
        nonSelectedIcon = Icons.Outlined.Home
    ),
    BottomNavigationItem(
        name = "Space",
        navigationRoute = NavigationRoutes.SPACE_SCREEN,
        selectedIcon = null,
        nonSelectedIcon = null,
        selectedIconRes = R.drawable.satellite_filled,
        nonSelectedIconRes = R.drawable.satellite_outlined
    ),
    BottomNavigationItem(
        name = "News",
        navigationRoute = NavigationRoutes.NEWS_SCREEN,
        selectedIcon = Icons.Filled.Newspaper,
        nonSelectedIcon = Icons.Outlined.Newspaper
    ),
    BottomNavigationItem(
        name = "Bookmarks",
        navigationRoute = NavigationRoutes.BOOKMARKS_SCREEN,
        selectedIcon = Icons.Filled.Bookmarks,
        nonSelectedIcon = Icons.Outlined.Bookmarks
    ),
)
@Composable
fun BottomNavigationComposable(
    navController: NavController
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    AppTheme {
        NavigationBar(modifier = Modifier.height(52.dp),containerColor = MaterialTheme.colorScheme.primary) {
            bottomNavDataList.forEach {
                val isSelected = currentDestination.toString() == it.navigationRoute
                val currentIconVector = if (isSelected) {
                    it.selectedIcon
                } else {
                    it.nonSelectedIcon
                }
                val currentIconRes = if (isSelected) {
                    it.selectedIconRes
                } else {
                    it.nonSelectedIconRes
                }
                NavigationBarItem(
                    selected = isSelected, onClick = {
                    if (!isSelected) {
                        navController.navigate(it.navigationRoute)
                    }
                }, icon = {
                    if (currentIconVector != null) {
                        Icon(
                            imageVector = currentIconVector,
                            contentDescription = it.name,
                            tint = if(isSelected) MaterialTheme.colorScheme.primary else  MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        currentIconRes?.let { it1 -> painterResource(id = it1) }?.let { it2 ->
                            Icon(
                                painter = it2,
                                contentDescription = it.name,
                                tint = if(isSelected) MaterialTheme.colorScheme.primary else  MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                })
            }
        }
    }
}