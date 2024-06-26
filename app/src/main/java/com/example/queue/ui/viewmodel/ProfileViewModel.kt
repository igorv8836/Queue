package com.example.queue.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.data.entities.Invitation
import com.example.queue.data.firebase.QueueFirestoreDB
import com.example.queue.domain.repositories.AccountRepository
import com.example.queue.domain.repositories.FirestoreRepository
import com.example.queue.domain.repositories.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel: ViewModel() {
    private val firestoreRep = FirestoreRepository()
    private val accRep = AccountRepository()
    private val queueRep = QueueRepository(QueueFirestoreDB())

    private val _helpingText = MutableStateFlow("")
    val helpingText = _helpingText.asStateFlow()
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()
    private val _photoFile = MutableStateFlow<File?>(null)
    val photoFile = _photoFile.asStateFlow()
    private val _invitations = MutableStateFlow<List<Invitation>>(emptyList())
    val invitations = _invitations.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading

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
            else
                _helpingText.value = "Никнейм успешно изменен"
        }
    }

    fun changePassword(lastPassword: String, newPassword: String){
        viewModelScope.launch {
            val res = accRep.changePassword(lastPassword, newPassword)
            if (res.isFailure){
                _helpingText.value = res.exceptionOrNull()?.message ?: ""
            } else {
                _helpingText.value = "Пароль успешно изменен"
            }
        }
    }

    fun getInvitations(){
        viewModelScope.launch {
            val res = queueRep.getInvitations()
            if (res.isSuccess){
                res.getOrNull()?.let {
                    _invitations.value = it
                }
            }
            _isLoading.value = false
        }
    }

    fun applyInvitation(queueId: String){
        viewModelScope.launch {
            val res = queueRep.applyInvitation(queueId)
            if (res.isSuccess){
                getInvitations()
            }
        }
    }

    fun declineInvitation(queueId: String){
        viewModelScope.launch {
            val res = queueRep.declineInvitation(queueId)
            if (res.isSuccess){
                getInvitations()
            }
        }
    }
}