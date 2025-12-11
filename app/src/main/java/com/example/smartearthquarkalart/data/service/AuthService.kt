package com.example.smartearthquarkalart.data.service

import com.example.smartearthquarkalart.data.models.LogIn
import com.example.smartearthquarkalart.data.models.Register
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface AuthService {

    fun userRegister(user: Register): Task<AuthResult>

    fun userLogin(user: LogIn): Task<AuthResult>

    fun createUser(user: Register): Task<Void>

}