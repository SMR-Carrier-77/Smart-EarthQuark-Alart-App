package com.example.smartearthquarkalart.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartearthquarkalart.core.DataState
import com.example.smartearthquarkalart.data.models.LogIn
import com.example.smartearthquarkalart.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val userAuth : AuthRepository
): ViewModel() {



    private val logInResponce = MutableLiveData<DataState<LogIn>>()

    val logIn_Responce : LiveData<DataState<LogIn>> = logInResponce

    fun userLogIn(user : LogIn){
        logInResponce.postValue(DataState.Loading())

        userAuth.userLogin(user).addOnSuccessListener {
            logInResponce.postValue(DataState.Success(user))
        }.addOnFailureListener {
            logInResponce.postValue(DataState.Error(it.message.toString()))
        }

    }

}