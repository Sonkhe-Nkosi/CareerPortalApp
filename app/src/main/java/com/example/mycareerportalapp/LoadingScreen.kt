package com.example.mycareerportalapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun LoadingScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { navController.navigate("HomeScreen") }
    ) {
        // Add background photo
        Image(
            painter = painterResource(id = R.drawable.background), // Replace with your background image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Flying logos animation
        FlyingLogosAnimation()
    }
}

@Composable
fun FlyingLogosAnimation() {
    val logos = listOf(
        R.drawable.university1, // Replace with your drawable resources
        R.drawable.logo22,
        R.drawable.university1, // Replace with your drawable resources
        R.drawable.logo44,
        R.drawable.logo11,
        R.drawable.university1,
        R.drawable.logo44, // Replace with your drawable resources
        R.drawable.logo22,
        R.drawable.university1, // Replace with your drawable resources
        R.drawable.logo22,
        R.drawable.logo11,
        R.drawable.university1, // Replace with your drawable resources
        R.drawable.logo22,
        R.drawable.logo11,
        R.drawable.logo44,
        R.drawable.university1, // Replace with your drawable resources
        R.drawable.logo22,
        R.drawable.logo11,// Replace with your drawable resources
        R.drawable.logo44,
        R.drawable.logo11,
        R.drawable.logo22,
        R.drawable.university1, // Replace with your drawable resources
        R.drawable.logo22,
        R.drawable.logo11
    )

    Box(modifier = Modifier.fillMaxSize()) {
        logos.forEachIndexed { index, logo ->
            FlyingLogo(
                imageRes = logo,
                initialOffsetX = (50 * index).dp,
                initialOffsetY = (100 * index).dp
            )
        }
    }
}

@Composable
fun FlyingLogo(imageRes: Int, initialOffsetX: Dp, initialOffsetY: Dp) {
    val transition = rememberInfiniteTransition()
    val randomXDirection = remember { if (Random.nextBoolean()) 1 else -1 }
    val randomYDirection = remember { if (Random.nextBoolean()) 1 else -1 }

    val offsetX by transition.animateFloat(
        initialValue = 2f,
        targetValue = 300f * randomXDirection, // Randomize horizontal direction
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val offsetY by transition.animateFloat(
        initialValue = 1f,
        targetValue = 300f * randomYDirection, // Randomize vertical direction
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        delay(100) // Optional delay for staggered effect
    }

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .offset(x = initialOffsetX + offsetX.dp, y = initialOffsetY + offsetY.dp)
            .size(200.dp), // Set a fixed size for the logos
        contentScale = ContentScale.Fit // Maintain the original shape of the logo
    )
}
