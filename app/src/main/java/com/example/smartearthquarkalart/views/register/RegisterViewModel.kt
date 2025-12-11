package com.example.smartearthquarkalart.views.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartearthquarkalart.core.DataState
import com.example.smartearthquarkalart.data.models.Register
import com.example.smartearthquarkalart.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private var auth : AuthRepository
) : ViewModel() {

    private var _registationResponce = MutableLiveData<DataState<Register>>()

    var registation_responce : LiveData<DataState<Register>> = _registationResponce

    fun userRegistation(user : Register){

        _registationResponce.postValue(DataState.Loading())

        auth.userRegister(user).addOnSuccessListener {

            it.user?.let{createdUser->
                user.userId = createdUser.uid

                auth.createUser(user).addOnSuccessListener {
                    _registationResponce.postValue(DataState.Success(user))
                }.addOnFailureListener {
                    _registationResponce.postValue(DataState.Error(it.message.toString()))
                }
            }

        }.addOnFailureListener {
            _registationResponce.postValue(DataState.Error(it.message))
        }

    }


}