package com.example.queue.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.queue.viewmodels.AuthViewModel
import com.example.queue.R
import com.example.queue.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        navController = findNavController()

        binding.registerButton.setOnClickListener{
            val bundle = Bundle()
            bundle.putString(
                "email",
                binding.editTextEmailInputText.text.toString())
            bundle.putString(
                "password",
                binding.editTextPasswordInputText.text.toString())
            navController.navigate(R.id.action_authFragment_to_registerFragment, bundle)
        }

        binding.authButton.setOnClickListener {
            viewModel.signIn(
                binding.editTextEmailInputText.text.toString(),
                binding.editTextPasswordInputText.text.toString())
        }

        viewModel.navigateToBaseFragment.observe(viewLifecycleOwner) {
            navController.navigate(R.id.action_authFragment_to_newsFragment)
        }

        viewModel.errorText.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), viewModel.errorText.value, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}