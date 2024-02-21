package com.example.queue.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.FragmentRegisterBinding
import com.example.queue.viewmodels.RegisterViewModel
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        navController = findNavController()

        binding.editTextEmailInputText.setText(arguments?.getString("email") ?: "")
        binding.editTextPasswordInputText.setText(arguments?.getString("password") ?: "")

        binding.registerButton.setOnClickListener{
            viewModel.registerAccount(
                binding.editTextEmailInputText.text.toString(),
                binding.editTextPasswordInputText.text.toString(),
                binding.editTextNicknameInputText.text.toString())
        }

        viewModel.navigateToBaseFragment.observe(viewLifecycleOwner) {
            if (it)
                navController.navigate(R.id.action_registerFragment_to_newsFragment)
        }

        viewModel.errorText.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, viewModel.errorText.value.toString(), Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}