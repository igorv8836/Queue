package com.example.queue.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.queue.ui.navigation.NavGraph
import com.example.queue.ui.navigation.RouteName
import com.example.queue.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(ComposeView(this).apply {
            setContent {
                MaterialTheme {
                    val navController = rememberNavController()
                    NavGraph(navHostController = navController)

                    val toMainScreen = viewModel.navigateToBaseFragment.collectAsState()

                    if (toMainScreen.value) {
                        navController.navigate(RouteName.MAIN_SCREEN.value) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                }

            }
        })
    }
}