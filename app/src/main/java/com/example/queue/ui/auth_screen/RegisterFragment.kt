package com.example.queue.ui.auth_screen

import android.app.Activity
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
import com.example.queue.model.listeners.ShowBottomMenuListener
import com.example.queue.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {
    private var showBottomMenuListener: ShowBottomMenuListener? = null
    private lateinit var viewModel: RegisterViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentRegisterBinding

    @Deprecated("Deprecated in Java")
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
            if (it) {
                navController.navigate(R.id.action_registerFragment_to_newsFragment)
                showBottomMenuListener?.onShow()
            }
        }

        viewModel.errorText.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), viewModel.errorText.value.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is ShowBottomMenuListener)
            showBottomMenuListener = activity
    }
}