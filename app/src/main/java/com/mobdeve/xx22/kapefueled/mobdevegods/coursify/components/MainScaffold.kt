package com.mobdeve.xx22.kapefueled.mobdevegods.coursify.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mobdeve.xx22.kapefueled.mobdevegods.coursify.Screen

@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable (Modifier) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = when (currentRoute) {
        Screen.Home.route,
        Screen.SavedPlans.route,
        Screen.Tracking.route,
        Screen.Profile.route -> true
        else -> false
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomBar(
                    selectedIndex = when (currentRoute) {
                        Screen.Home.route -> 0
                        Screen.SavedPlans.route -> 1
                        Screen.Tracking.route -> 2
                        Screen.Profile.route -> 3
                        else -> 0
                    },
                    onItemSelected = { index ->
                        when (index) {
                            0 -> navController.navigate(Screen.Home.route)
                            1 -> navController.navigate(Screen.SavedPlans.route)
                            2 -> navController.navigate(Screen.Tracking.route)
                            3 -> navController.navigate(Screen.Profile.route)
                        }
                    },
                    onFabClick = {
                        navController.navigate(Screen.NewLearningPlan.route)
                    }
                )
            }
        }
    ) { paddingValues ->
        content(Modifier.padding(paddingValues))
    }
}