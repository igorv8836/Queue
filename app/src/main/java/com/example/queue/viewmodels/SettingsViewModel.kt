package com.example.queue.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.queue.repositories.AccountRepository
import com.example.queue.repositories.FirestoreRepository
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import java.io.File

class SettingsViewModel: ViewModel() {
    private val firestoreRep = FirestoreRepository
    private val accRep = AccountRepository
    private val _helpingText = MutableLiveData<String>()
    val helpingText: LiveData<String> = _helpingText
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname
    private val _image = MutableLiveData<File>()
    val image: LiveData<File> = _image
    private val _showAuth = MutableLiveData<Boolean>()
    val showAuth: LiveData<Boolean> = _showAuth
    private val _photoFile = MutableLiveData<File>()
    val photoFile: LiveData<File> = _photoFile

    init {
        getEmail()
        getNickname()
//        getPhoto()
    }

    private fun getEmail(){
        val res = accRep.getEmail()
        res?.let { _email.value = it }
    }

    private fun getNickname(){
        viewModelScope.launch {
            val res = firestoreRep.getNickname()
            if (res.isSuccess){
                _nickname.postValue(res.getOrNull())
            } else {
                _helpingText.postValue(res.exceptionOrNull()?.message)
            }
        }
    }

    private fun getPhoto(ref: StorageReference){
        viewModelScope.launch {
            val res = firestoreRep.getUserImage(ref)
            if (res.isSuccess){
                if (res.getOrNull() != null)
                    _photoFile.postValue(res.getOrNull())
                else
                    _helpingText.postValue("Фотка не загружена")
            } else{
                _helpingText.postValue(res.exceptionOrNull()?.message)
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
                res.getOrNull()?.let { getPhoto(it) }
            } else {
                _helpingText.postValue(res.exceptionOrNull()?.message)
            }
        }
    }
}