package com.example.queue.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.queue.ui.base_screens.auth_screen.AuthScreen
import com.example.queue.ui.base_screens.auth_screen.RegisterScreen
import com.example.queue.ui.base_screens.InvitationScreen
import com.example.queue.ui.base_screens.MainScreen
import com.example.queue.ui.base_screens.queue_screen.QueueScreen

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "start") {

        composable("start"){

        }

        composable(RouteName.AUTH_SCREEN.value) {
            AuthScreen(navHostController)
        }

        composable(RouteName.REGISTER_SCREEN.value) {
            RegisterScreen(navController = navHostController, viewModel = viewModel())
        }

        composable(RouteName.MAIN_SCREEN.value) {
            val bottomNavController = rememberNavController()
            MainScreen(navHostController, bottomNavController)
        }

        composable(
            "${RouteName.QUEUE_SCREEN.value}/{queueId}",
            arguments = listOf(navArgument("queueId") { type = NavType.StringType })
        ) {
            QueueScreen(
                it.arguments?.getString("queueId"),
                viewModel = viewModel(),
                navController = navHostController
            )
        }

        composable(RouteName.INVITATION_SCREEN.value){
            InvitationScreen(navController = navHostController, viewModel())
        }
    }
}