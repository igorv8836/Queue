package com.example.queue.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.model.repositories.AccountRepository
import com.example.queue.model.repositories.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel: ViewModel() {
    private val firestoreRep = FirestoreRepository
    private val accRep = AccountRepository
    private val _helpingText = MutableStateFlow("")
    val helpingText = _helpingText.asStateFlow()
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()
    private val _image = MutableStateFlow<File?>(null)
    val image = _image.asStateFlow()
    private val _showAuth = MutableStateFlow(false)
    val showAuth = _showAuth.asStateFlow()
    private val _photoFile = MutableStateFlow<File?>(null)
    val photoFile = _photoFile.asStateFlow()

    init {
        getEmail()
        getNickname()
        getPhoto()
    }

    private fun getEmail(){
        val res = accRep.getEmail()
        res?.let { _email.value = it }
    }

    private fun getNickname(){
        viewModelScope.launch {
            firestoreRep.getNickname().collect{
                _nickname.value = it
            }
        }
    }

    private fun getPhoto(){
        viewModelScope.launch {
            val res = firestoreRep.getUserImage()
            if (res.isSuccess){
                val file = res.getOrNull()
                if (file != null)
                    _photoFile.value = file
            } else{
                _helpingText.value = res.exceptionOrNull()?.message ?: ""
            }
        }
    }

    fun signOut(){
        accRep.signOut()
        _showAuth.value = true
    }

    fun changePhoto(photoUri: Uri){
        viewModelScope.launch {
            val res = firestoreRep.setPhoto(photoUri)
            if (res.isSuccess){
                getPhoto()
            }
        }
    }

    fun changeNickname(nickname: String){
        viewModelScope.launch {
            val res = firestoreRep.changeNickname(nickname)
            if (res.isFailure)
                _helpingText.value = res.exceptionOrNull()?.message ?: ""
        }
    }

    fun changePassword(password: String){
        viewModelScope.launch {
            val res = accRep.changePassword(password)
            if (res.isFailure){
                _helpingText.value = res.exceptionOrNull()?.message ?: ""
            }
        }
    }
}