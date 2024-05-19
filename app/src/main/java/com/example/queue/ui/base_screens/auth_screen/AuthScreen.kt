package com.example.queue.ui.base_screens.auth_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.queue.R
import com.example.queue.ui.Dimens
import com.example.queue.ui.navigation.RouteName
import com.example.queue.ui.viewmodel.AuthViewModel

@SuppressLint("RememberReturnType")
@Composable
fun AuthScreen(navController: NavController) {
    val viewModel = remember { AuthViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    var showRecoveryDialog by remember { mutableStateOf(false) }
    var recoveryTextField by remember { mutableStateOf("") }
    val toMainScreen by viewModel.navigateToBaseFragment.collectAsState()

    if (toMainScreen){
        navController.navigate(RouteName.MAIN_SCREEN.value) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.helpingText.collect {
            it?.let { snackbarHostState.showSnackbar(it) }
        }
    }

    if (showRecoveryDialog) {
        AlertDialog(
            title = { Text("Восстановление пароля") },
            onDismissRequest = { showRecoveryDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRecoveryDialog = false
                        viewModel.forgotPassword(recoveryTextField)
                    }
                ) {
                    Text("Отправить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRecoveryDialog = false }
                ) {
                    Text("Отмена")
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = recoveryTextField,
                        onValueChange = { recoveryTextField = it },
                        label = { Text("Введите почту") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(
                modifier = Modifier
                    .padding(Dimens.small)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .padding(top = Dimens.large)
                        .size(240.dp)
                )

                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }


                OutlinedTextField(
                    value = email,
                    onValueChange = { it1 -> email = it1 },
                    label = { Text("Введите почту") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.large, bottom = Dimens.small)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { it1 -> password = it1 },
                    label = { Text("Введите пароль") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { password = "" }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear text")
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    onClick = { showRecoveryDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Забыли пароль?")
                }

                Spacer(modifier = Modifier.height(Dimens.tiny))

                TextButton(
                    onClick = { viewModel.signIn(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Войти")
                }

                TextButton(
                    onClick = {
                        navController.navigate(RouteName.REGISTER_SCREEN.value)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Зарегистрироваться")
                }
            }
        }

    }


}
