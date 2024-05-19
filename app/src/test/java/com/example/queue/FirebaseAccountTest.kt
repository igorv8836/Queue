package com.example.queue

import com.example.queue.data.firebase.FirebaseAccount
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.test.Test

class FirebaseAccountTest {


    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var firebaseAccount: FirebaseAccount

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        firebaseAccount = FirebaseAccount(firebaseAuth)
    }

    @Test
    fun `test signIn calls FirebaseAuth`() = runTest {
        firebaseAccount.signInAccount("test@example.com", "password")
        verify(firebaseAuth).signInWithEmailAndPassword("test@example.com", "password")
    }
}