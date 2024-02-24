package com.example.queue.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.ActivityMainBinding
import com.example.queue.viewmodels.MainViewModel
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.progressBar.visibility = View.VISIBLE
        viewModel.checkAuth()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host)
        val navController = navHostFragment?.findNavController()
        viewModel.missAuth.observe(this){
            if (it){
                navController?.navigate(R.id.action_authFragment_to_newsFragment)
                binding.navMenu.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
        }

        binding.navMenu.setOnItemSelectedListener(object: NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu_news -> {
                        navController
                            ?.navigate(R.id.action_authFragment_to_newsFragment)
                        return true
                    }

                    R.id.menu_search -> {
                        navController
                            ?.navigate(R.id.action_newsFragment_to_searchFragment)
                        return true
                    }

                    R.id.menu_queues -> {
                        navController
                            ?.navigate(R.id.action_newsFragment_to_queuesFragment)
                        return true
                    }

                    R.id.menu_settings -> {
                        navController
                            ?.navigate(R.id.action_newsFragment_to_settingsFragment)
                        return true
                    }
                }
                return false
            }
        })

        supportActionBar?.hide()
        binding.navMenu.visibility


    }
}