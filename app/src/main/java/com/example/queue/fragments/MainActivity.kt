package com.example.queue.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.ActivityMainBinding
import com.example.queue.viewmodels.AuthViewModel
import com.example.queue.viewmodels.MainViewModel

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

        viewModel.missAuth.observe(this){
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host)
            val navController = navHostFragment?.findNavController()
            if (it){
                navController?.navigate(R.id.action_authFragment_to_newsFragment)
                binding.navMenu.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
        }

        supportActionBar?.hide()
        binding.navMenu.visibility


    }
}