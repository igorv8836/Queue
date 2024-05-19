package com.example.queue.ui.base_screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.queue.ui.navigation.bottom_navigation.BottomNavGraph
import com.example.queue.ui.navigation.bottom_navigation.BottomNavigation

@Composable
fun MainScreen(navController: NavController, bottomNavController: NavHostController){
    MaterialTheme {
        Scaffold(
            bottomBar = { BottomNavigation(bottomNavController) },
            modifier = Modifier.testTag("main_screen")
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)){
                BottomNavGraph(bottomNavController, navController)
            }

        }
    }


}