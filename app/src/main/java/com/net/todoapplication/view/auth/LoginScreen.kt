package com.net.todoapplication.view.auth

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dewan.todoapp.util.Validator
import com.google.firebase.auth.FirebaseAuth
import com.net.todoapplication.R
import com.net.todoapplication.utils.ClassName
import com.net.todoapplication.viewmodel.auth.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val authResult = viewModel.auth

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = {
                    emailOrPhone = it
                    isEmailValid = Validator.validateEmail(it)
                },
                label = { Text(text = "Email or Phone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = !isEmailValid
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordValid = Validator.validatePassword(it)
                },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !isPasswordValid
            )

            Spacer(modifier = Modifier.height(16.dp))

            //LoginButton
            Button(
                onClick = {
                    isLoading = true

                    viewModel.login(emailOrPhone, password, onResult = {
                        isLoading = false
                        if (it == "Sign-in Successful") {
                            navController.navigate(ClassName.HOME_SCREEN) {
                                popUpTo(ClassName.LOGIN_SCREEN) {
                                    inclusive = true
                                }
                            }
                        }else{
                            Log.d("LoginScreen", "Login failed: $it")
                            Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
                        }
                    })


                },
                enabled = isEmailValid && isPasswordValid,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            ) {
                Text(text = if (isLoading) "Logging in..." else "Login")
            }

            Spacer(modifier = Modifier.height(25.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

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


@Preview
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    val viewModel: LoginViewModel = hiltViewModel()
    LoginScreen(navController = navController, viewModel)

}
