package com.example.queue.ui.settings_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.queue.R
import com.example.queue.ui.Dimens
import com.example.queue.viewmodel.SettingsViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val email = viewModel.email.collectAsState()
    val nickname = viewModel.nickname.collectAsState()
    val photoFile = viewModel.photoFile.collectAsState()
    val helpingText = viewModel.helpingText.collectAsState()

    Column(
        modifier = Modifier
            .padding(start = Dimens.small, end = Dimens.small, top = Dimens.small)
            .fillMaxSize()
    ) {
        GlideImage(
            model = photoFile.value?.toURI().toString(),
            contentDescription = "user photo",
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
                .background(color = Color.Gray, shape = CircleShape)
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
            onClick = { viewModel.signOut() },
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
                SettingsItem("Приглашения")
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
fun SettingsItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Обработка клика */ }
            .padding(top = Dimens.medium, bottom = Dimens.medium, start = Dimens.medium),
        textAlign = TextAlign.Left
    )
    HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(start = Dimens.small))
}
