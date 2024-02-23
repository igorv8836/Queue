package com.example.queue.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.FragmentAuthBinding
import com.example.queue.viewmodels.AuthViewModel

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

        viewModel.messageText.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), viewModel.messageText.value, Toast.LENGTH_LONG).show()
        }

        binding.forgotPasswordButton.setOnClickListener {
            val editText = EditText(requireContext())
            editText.hint = "Введите почту"

            val customViewDialog = AlertDialog.Builder(requireContext())
                .setTitle("Восстановление пароля")
                .setNegativeButton("Назад", null)
                .setPositiveButton("Отправить"
                ) { dialog, which -> viewModel.forgotPassword(editText.text.toString()) }
                .setView(editText)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}