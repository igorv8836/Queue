package com.example.queue

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.queue.ui.MainActivity
import com.example.queue.ui.base_screens.MainScreen
import com.example.queue.ui.navigation.NavGraph
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testMainScreenIsDisplayed() {
        composeTestRule.setContent {
            MaterialTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController, bottomNavController = navController)
            }
        }

        composeTestRule.onNodeWithTag("main_screen").assertIsDisplayed()
    }
}