package com.example.queue.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.FragmentSettingsBinding
import com.example.queue.viewmodels.SettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel){
            helpingText.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

            email.observe(viewLifecycleOwner){
                binding.emailTextview.text = it
            }

            nickname.observe(viewLifecycleOwner){
                binding.nicknameTextview.text = it
            }

            showAuth.observe(viewLifecycleOwner){
                if (it)
                    navController.navigate(R.id.authFragment)
            }
        }

        binding.signOutButton.setOnClickListener { viewModel.signOut() }
    }
}