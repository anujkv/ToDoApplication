package com.net.todoapplication.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(val auth: FirebaseAuth): ViewModel() {
    private val _authResult = MutableLiveData<String>()
    val authResult: LiveData<String> = _authResult

    fun register(email: String, password: String,confirmPassword: String, onResult: (String) -> Unit){
        //Input Validation check
        if (email.isEmpty() && password.isEmpty()){
            onResult("Authentication Failed: Email & password cannot be empty")
            return
        }
        if(email.isEmpty()){
            onResult("Authentication Failed: Email cannot be empty")
            return
        }
        if(password.isEmpty()){
            onResult("Authentication Failed: Password cannot be empty")
        }
        if(confirmPassword.isEmpty()){
            onResult("Authentication Failed: Confirm Password cannot be empty")
        }
        if(password.trim() == confirmPassword.trim()){
            onResult("Password Mismatched: Password and Confirm Password should be same")
        }

        //Firebase Authentication
        auth.createUserWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.value = "Registration Successful"
                    onResult("Registration Successful")
                } else {
                    _authResult.value = "Registration Failed: ${task.exception?.message}"
                    onResult("Registration Failed: ${task.exception?.message}")
                }
            }
    }
}