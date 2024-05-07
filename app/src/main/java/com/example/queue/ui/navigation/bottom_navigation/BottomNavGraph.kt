package com.example.queue.ui.navigation.bottom_navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.queue.ui.base_screens.NewsScreen
import com.example.queue.ui.base_screens.QueuesScreen
import com.example.queue.ui.navigation.RouteName
import com.example.queue.ui.base_screens.profile_screen.SettingsScreen

@Composable
fun BottomNavGraph(
    navHostController: NavHostController,
    baseNavController: NavController
) {
    NavHost(navController = navHostController, startDestination = RouteName.NEWS_SCREEN.value) {
        composable(RouteName.NEWS_SCREEN.value) {
            NewsScreen(viewModel())
        }

        composable(RouteName.QUEUES_SCREEN.value) {
            QueuesScreen(viewModel = viewModel(), navController = baseNavController)
        }

        composable(RouteName.PROFILE_SCREEN.value) {
            SettingsScreen(viewModel = viewModel(), navController = baseNavController)
        }
    }
}