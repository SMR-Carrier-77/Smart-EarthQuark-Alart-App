package com.example.smartearthquarkalart.data.repository

import android.util.Log
import com.example.smartearthquarkalart.data.models.LogIn
import com.example.smartearthquarkalart.data.models.Register
import com.example.smartearthquarkalart.data.service.AuthService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val jjAuth : FirebaseAuth,
    private val db : FirebaseFirestore
): AuthService {

    override fun userRegister(user: Register) : Task<AuthResult> {

        return jjAuth.createUserWithEmailAndPassword(user.email , user.password)

    }

    override fun userLogin(user: LogIn): Task<AuthResult> {

        return jjAuth.signInWithEmailAndPassword(user.email , user.password)

    }

    override fun createUser(user: Register) : Task<Void> {

        return db.collection("User").document(user.userId).set(user)

    }

}