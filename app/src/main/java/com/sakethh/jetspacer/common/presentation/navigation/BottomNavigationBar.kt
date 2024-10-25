package com.sakethh.jetspacer.common.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HdrOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(selected = true, onClick = {}, icon = {
            Icon(Icons.Outlined.HdrOn, null)
        })
        NavigationBarItem(selected = true, onClick = {}, icon = {
            Icon(Icons.Outlined.HdrOn, null)
        })
        NavigationBarItem(selected = true, onClick = {}, icon = {
            Icon(Icons.Outlined.HdrOn, null)
        })
        NavigationBarItem(selected = true, onClick = {}, icon = {
            Icon(Icons.Outlined.HdrOn, null)
        })
    }
}