package com.example.queue.ui.base_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.queue.add_classes.Invitation
import com.example.queue.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationScreen(navController: NavController, viewModel: ProfileViewModel) {
    viewModel.getInvitations()
    val invitations by viewModel.invitations.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Приглашения") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(invitations) { invitation ->
                InvitationItem(invitation, viewModel)
            }
        }
    }
}

@Composable
fun InvitationItem(invitation: Invitation, viewModel: ProfileViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = invitation.queueName,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Количество людей: ${invitation.peopleCount}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Автор: ${invitation.author}",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = { viewModel.declineInvitation(invitation.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Red),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Отклонить")
                }
                TextButton(
                    onClick = { viewModel.applyInvitation(invitation.id) },
                ) {
                    Text("Принять")
                }
            }
        }
    }
}
