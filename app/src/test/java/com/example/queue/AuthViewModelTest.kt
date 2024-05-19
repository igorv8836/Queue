package com.example.queue

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.queue.domain.repositories.AccountRepository
import com.example.queue.ui.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test


@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var authViewModel: AuthViewModel
    private lateinit var accountRepository: AccountRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        accountRepository = mockk()
        authViewModel = AuthViewModel(accountRepository)
        Dispatchers.setMain((UnconfinedTestDispatcher()))
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test login successful`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { accountRepository.signInAccount(any(), any()) } returns Result.success(Unit)

        authViewModel.navigateToBaseFragment.test {
            authViewModel.signIn("test@example.com", "password")
            advanceUntilIdle()
            skipItems(1)
            assertTrue(awaitItem())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test login failure`() = runTest(UnconfinedTestDispatcher()) {
        val errorMessage = "Неверный логин или пароль"
        coEvery { accountRepository.signInAccount(any(), any()) } returns Result.failure(
            FirebaseAuthInvalidCredentialsException(errorMessage, errorMessage)
        )

        authViewModel.helpingText.test {
            authViewModel.signIn("test@example.com", "password")
            advanceUntilIdle()
            assertEquals(errorMessage, awaitItem())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}