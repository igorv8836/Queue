package com.example.queue.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.EditTextBinding
import com.example.queue.databinding.FragmentSettingsBinding
import com.example.queue.viewmodels.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

class SettingsFragment : Fragment() {
    private val READ_MEDIA_IMAGES_PERMISSION = 1
    private val GALLERY_START_CODE = 2
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
            email.observe(viewLifecycleOwner){ binding.emailTextview.text = it }
            nickname.observe(viewLifecycleOwner){ binding.nicknameTextview.text = it }
            showAuth.observe(viewLifecycleOwner){
                if (it)
                    navController.navigate(R.id.authFragment)
            }
            photoFile.observe(viewLifecycleOwner){
                Picasso.get().load(it).resize(
                    resources.getDimensionPixelSize(R.dimen.user_image_size),
                    resources.getDimensionPixelSize(R.dimen.user_image_size)
                ).centerCrop().into(binding.accountImage)
            }
        }

        val dialogBinding = EditTextBinding.bind(
            layoutInflater.inflate(R.layout.edit_text, null))

        with(binding){
            signOutButton.setOnClickListener { viewModel.signOut() }
            accountImage.setOnClickListener { changePhoto() }
            changeNicknameButton.setOnClickListener{
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Изменение никнейма")
                    .setView(dialogBinding.root)
                    .setNegativeButton("Отмена") { _, _ -> }
                    .setPositiveButton("Сохранить") { _, _ ->
                        viewModel.changeNickname(dialogBinding.inputEditText.text.toString())
                    }
                    .show()
            }
            changePasswordButton.setOnClickListener {
                dialogBinding.textField.hint = "Введите новый пароль"

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Изменение никнейма")
                    .setView(dialogBinding.root)
                    .setNegativeButton("Отмена") { _, _ -> }
                    .setPositiveButton("Изменить") { _, _ ->
                        viewModel.changePassword(dialogBinding.inputEditText.text.toString())
                    }
                    .show()
            }
        }
    }

    private fun changePhoto() {
        val permission = when {
            Build.VERSION.SDK_INT >= 33 -> android.Manifest.permission.READ_MEDIA_IMAGES
            else -> android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(permission), READ_MEDIA_IMAGES_PERMISSION)
        } else {
            startActivityForResult(
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                GALLERY_START_CODE
            )
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_MEDIA_IMAGES_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, GALLERY_START_CODE)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_START_CODE && resultCode == Activity.RESULT_OK && data != null) {
            data.data?.let { viewModel.changePhoto(it) }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}