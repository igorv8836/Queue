package com.example.queue.ui.base_screens.profile_screen

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.queue.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SettingsScreen(viewModel: ProfileViewModel, navController: NavController) {
    val email = viewModel.email.collectAsState()
    val nickname = viewModel.nickname.collectAsState()
    val photoFile = viewModel.photoFile.collectAsState()
    val helpingText = viewModel.helpingText.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var showNicknameDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    if (showNicknameDialog) {
        ProfileAdditionalDialog(
            title = "Изменение никнейма",
            textFieldLabel = "Введите новый никнейм",
            denyRequest = { showNicknameDialog = false }) {
            viewModel.changeNickname(it)
            showNicknameDialog = false
        }
    }

    if (showPasswordDialog) {
        ChangePasswordDialog(
            denyRequest = { showPasswordDialog = false }) { last, new ->
            viewModel.changePassword(last, new)
            showPasswordDialog = false
        }
    }

    LaunchedEffect(helpingText.value) {
        coroutineScope.launch {
            if (helpingText.value.isNotEmpty())
                snackbarHostState.showSnackbar(helpingText.value)
        }
    }

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



    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier
            .padding(top = Dimens.small)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
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
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = Dimens.small)
            ) {
                Column(
                    modifier = Modifier
                        .padding(Dimens.small)
                        .verticalScroll(scrollState)
                ) {
                    SectionTitle(title = "Профиль")
                    SettingsItem("Приглашения") {
                        navController.navigate(RouteName.INVITATION_SCREEN.value)
                    }
                    SectionTitle(title = "Настройки аккаунта")
                    SettingsItem("Изменить никнейм") {
                        showNicknameDialog = true
                    }
                    SettingsItem("Изменить пароль") {
                        showPasswordDialog = true
                    }
                }

            }
        }
    }

}