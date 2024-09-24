package com.net.todoapplication.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.net.todoapplication.utils.ClassName
import com.net.todoapplication.view.auth.LoginScreen
import com.net.todoapplication.view.auth.RegistrationScreen
import com.net.todoapplication.view.home.HomeScreen
import com.net.todoapplication.view.splash.SplashScreen
import com.net.todoapplication.viewmodel.auth.LoginViewModel

@Composable
fun AppNavigation() {
 val navController =  rememberNavController()
 val viewModel: LoginViewModel = hiltViewModel()
 
 NavHost(navController = navController, startDestination = ClassName.SPLASH_SCREEN ){
  composable(ClassName.SPLASH_SCREEN){ SplashScreen(navController) }
  composable(ClassName.LOGIN_SCREEN){ LoginScreen(navController, viewModel) }
  composable(ClassName.REGISTRATION_SCREEN){ RegistrationScreen(navController) }
  composable(ClassName.HOME_SCREEN){ HomeScreen(navController) }

 }

}