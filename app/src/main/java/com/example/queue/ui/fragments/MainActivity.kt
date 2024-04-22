package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.ActivityMainBinding
import com.example.queue.model.listeners.ShowBottomMenuListener
import com.example.queue.viewmodel.MainViewModel
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), ShowBottomMenuListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host)
        val navController = navHostFragment?.findNavController()
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()

        viewModel.navigateToBaseFragment.observe(this){
            if (it) {
                navController?.navigate(R.id.action_authFragment_to_newsFragment, null, navOptions)
                binding.navMenu.visibility = View.VISIBLE
            }
        }

        binding.navMenu.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu_news -> {
                        navController?.navigate(R.id.action_to_newsFragment, null, navOptions)
                        return true
                    }

                    R.id.menu_search -> {
                        navController?.navigate(R.id.action_to_searchFragment, null, navOptions)
                        return true
                    }

                    R.id.menu_queues -> {
                        navController?.navigate(R.id.action_to_queuesFragment, null, navOptions)
                        return true
                    }

                    R.id.menu_settings -> {
                        navController?.navigate(R.id.action_to_settingsFragment, null, navOptions)
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onShow() {
        binding.navMenu.visibility = View.VISIBLE
    }
}