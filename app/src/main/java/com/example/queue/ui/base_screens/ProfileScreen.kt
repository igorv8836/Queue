package com.example.queue.ui.base_screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.queue.ui.Dimens
import com.example.queue.ui.navigation.RouteName
import com.example.queue.viewmodel.ProfileViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SettingsScreen(viewModel: ProfileViewModel, navController: NavController) {
    val email = viewModel.email.collectAsState()
    val nickname = viewModel.nickname.collectAsState()
    val photoFile = viewModel.photoFile.collectAsState()
    val helpingText = viewModel.helpingText.collectAsState()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.changePhoto(it) }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                pickImageLauncher.launch("image/*")
            }
        }
    )


    Column(
        modifier = Modifier
            .padding(start = Dimens.small, end = Dimens.small, top = Dimens.small)
            .fillMaxSize()
    ) {
        GlideImage(
            model = photoFile.value?.toURI().toString(),
            contentDescription = "user photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
                .background(color = Color.Gray, shape = CircleShape)
                .clickable {
                    permissionLauncher.launch(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                    )
                }
        )

        Text(
            text = nickname.value,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.small)
        )
        Text(
            text = email.value,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                viewModel.signOut()
                navController.navigate(RouteName.AUTH_SCREEN.value) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = Dimens.small),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Выйти")
        }

        Surface(
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(Dimens.small)
            ) {
                SectionTitle(title = "Профиль")
                SettingsItem("Приглашения"){
                    navController.navigate(RouteName.INVITATION_SCREEN.value)
                }
                SectionTitle(title = "Настройки аккаунта")
                SettingsItem("Изменить никнейм")
                SettingsItem("Изменить пароль")
                SectionTitle(title = "Общие настройки")
                SettingsItem("Настройки")
            }

        }

    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = Dimens.small)
    )
}

@Composable
fun SettingsItem(text: String, click: () -> Unit = {}) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { click() }
            .padding(top = Dimens.medium, bottom = Dimens.medium, start = Dimens.medium),
        textAlign = TextAlign.Left
    )
    HorizontalDivider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.padding(start = Dimens.small)
    )
}