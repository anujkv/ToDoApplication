package com.net.todoapplication.view.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.net.todoapplication.R
import com.net.todoapplication.utils.ClassName
import com.net.todoapplication.viewmodel.auth.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()){
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    /*val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    val authResult = viewModel.authResult*/
    val authResult = viewModel.auth

    /*val auth = FirebaseAuth.getInstance()*/
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center){

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                Modifier.size(80.dp))
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = { emailOrPhone = it},
                label = { Text(text = "Email or Phone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it},
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))

            Spacer(modifier = Modifier.height(16.dp))

            //LoginButton
            Button(onClick = {
                isLoading =true
                /*signIn(auth, emailOrPhone, password, onResult = {
                    isLoading =false
                    Log.e("Login", it)
                })*/

                viewModel.login(emailOrPhone,password, onResult = {
                    isLoading = false
                    navController.navigate(ClassName.HOME_SCREEN){
                        popUpTo(ClassName.LOGIN_SCREEN){
                            inclusive = true
                        }
                    }
                })



             },
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                Text(text = if (isLoading) "Logging in..." else "Login")
            }

            Spacer(modifier = Modifier.height(25.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){

                Text(text = "New User? ")

                Text(
                    text = "Create Account",
                    color = colorResource(id = R.color.blue),
                textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate(ClassName.REGISTRATION_SCREEN)
                    })
            }

        }
    }

}

fun signIn(auth: FirebaseAuth, emailOrPhone: String, password: String, onResult: (String) -> Unit) {
    auth.signInWithEmailAndPassword(emailOrPhone,password)
        .addOnCompleteListener {
            println(it.result)
            if (it.isSuccessful){
                println(it.result)
                onResult("Login Successful")
            }else{
                onResult("Authentication Failed")

            }
        }
}



/*
@Preview
@Composable
fun LoginScreenPreview(){
    val navController = rememberNavController()
    val viewModel = LoginViewModel(hiltViewModel())
    LoginScreen(navController = navController,viewModel)

}*/
