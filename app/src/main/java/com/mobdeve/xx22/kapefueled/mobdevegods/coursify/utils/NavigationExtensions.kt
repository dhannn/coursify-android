package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.navigateToBottomBarRoute(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
