package com.example.queue.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.ActivityMainBinding
import com.example.queue.model.listeners.ShowBottomMenuListener
import com.example.queue.ui.navigation.NavGraph
import com.example.queue.viewmodel.MainViewModel
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(ComposeView(this).apply {
            setContent {
                val navController = rememberNavController()
                NavGraph(navHostController = navController)
            }
        })
    }
}