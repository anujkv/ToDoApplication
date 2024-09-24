package com.net.todoapplication.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor(val auth: FirebaseAuth) :
    ViewModel() {
    private val _authResult = MutableLiveData<String>()
    val authResult: LiveData<String> = _authResult
//    val auth = FirebaseAuth.getInstance()
    fun login(email: String, password: String, onResult: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.value = "Sign-in Successful"
                    onResult("Sign-in Successful")

                } else {
                    _authResult.value = "Authentication Failed: ${task.exception?.message}"
                    onResult("Sign-in Successful")

                }
            }
    }
}