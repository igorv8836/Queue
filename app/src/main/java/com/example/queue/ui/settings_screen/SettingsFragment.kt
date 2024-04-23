package com.example.queue.ui.settings_screen

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
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.EditTextBinding
import com.example.queue.viewmodel.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private val READ_MEDIA_IMAGES_PERMISSION = 1
    private val GALLERY_START_CODE = 2
    private lateinit var viewModel: SettingsViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        navController = findNavController()
        return ComposeView(requireContext()).apply {
            setContent {
                SettingsScreen(viewModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            with(viewModel){
                helpingText.collect{
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
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

    private fun showEditDialog(
        title: String,
        hint: String,
        onPositiveClick: (String) -> Unit
    ) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.edit_text, null)
        val dialogBinding = EditTextBinding.bind(dialogView)
        dialogBinding.textField.hint = hint

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(dialogBinding.root)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Сохранить") { _, _ ->
                onPositiveClick(dialogBinding.inputEditText.text.toString())
            }
            .show()
    }
}