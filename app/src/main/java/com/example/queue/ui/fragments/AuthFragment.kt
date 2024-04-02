package com.example.queue.ui.fragments

import android.app.Activity
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
import com.example.queue.listeners.ShowBottomMenuListener
import com.example.queue.viewmodels.AuthViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class AuthFragment : Fragment() {
    private var showBottomMenuListener: ShowBottomMenuListener? = null
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

    @Deprecated("Deprecated in Java")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is ShowBottomMenuListener)
            showBottomMenuListener = activity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        navController = findNavController()


        with(binding){
            registerButton.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("email", binding.editTextEmailInputText.text.toString())
                bundle.putString("password", binding.editTextPasswordInputText.text.toString())
                navController.navigate(R.id.action_authFragment_to_registerFragment, bundle)
            }

            authButton.setOnClickListener {
                viewModel.signIn(
                    binding.editTextEmailInputText.text.toString(),
                    binding.editTextPasswordInputText.text.toString()
                )
            }

            forgotPasswordButton.setOnClickListener {
                val editText = EditText(requireContext())
                editText.hint = "Введите почту"

                AlertDialog.Builder(requireContext())
                    .setTitle("Восстановление пароля")
                    .setNegativeButton("Назад", null)
                    .setPositiveButton("Отправить"
                    ) { dialog, which -> viewModel.forgotPassword(editText.text.toString()) }
                    .setView(editText)
                    .show()
            }
        }

        with(viewModel){
            navigateToBaseFragment.observe(viewLifecycleOwner) {
                if (it) {
                    navController.navigate(R.id.action_authFragment_to_newsFragment)
                    showBottomMenuListener?.onShow()
                }
            }

            helpingText.observe(viewLifecycleOwner){
                Toast.makeText(requireContext(), viewModel.helpingText.value, Toast.LENGTH_LONG).show()
            }
        }
    }
}