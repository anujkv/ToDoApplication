package com.net.todoapplication.view.splash

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.net.todoapplication.R
import com.net.todoapplication.utils.ClassName
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Scaffold(topBar = {},
        containerColor = colorResource(id = R.color.white)) { innerPadding ->

        var startAnimation by remember { mutableStateOf(false) }

        val alphaAnimation by animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 5000)
        )
        innerPadding.calculateTopPadding()

        LaunchedEffect(Unit) {
            startAnimation = true
            delay(500)
            navigateToLoginScreen(navController)
        }
        
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            val logo = painterResource(id = R.drawable.logo)
            Image(painter = logo, contentDescription = "Logo",
                modifier = Modifier
                    .alpha(alphaAnimation)
                    .size(200.dp)
                    .graphicsLayer(alpha = alphaAnimation) )

        }

    }
}

fun navigateToLoginScreen(navController: NavController) {
    navController.navigate(ClassName.LOGIN_SCREEN){
        popUpTo(ClassName.SPLASH_SCREEN){
            inclusive = true
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    val navController = rememberNavController()
    SplashScreen(navController = navController)
}