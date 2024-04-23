package com.example.queue.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.queue.ui.auth_screen.AuthScreen

@Composable
fun NavGraph(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = RouteName.AUTH_SCREEN.value){
        composable(RouteName.AUTH_SCREEN.value){
            AuthScreen(navHostController)
        }

        composable(RouteName.REGISTER_SCREEN.value){

        }

        composable(RouteName.NEWS_SCREEN.value){

        }

        composable(RouteName.SEARCH_SCREEN.value){

        }

        composable(RouteName.QUEUES_SCREEN.value){

        }

        composable(RouteName.PROFILE_SCREEN.value){

        }
    }
}