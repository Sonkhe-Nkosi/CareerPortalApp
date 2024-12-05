package com.example.mycareerportalapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoFactorAuthenticationScreen(navController: NavController) {
    Scaffold(
        topBar = { GenericTopBar(navController, "Two step verification") },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Title
                Text(
                    text = "Secure your account with two-step verification",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Description 1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.security_privacy), // Add the appropriate icon here
                        contentDescription = "Lock Icon",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Two step verification gives you additional security by requiring a verification code whenever you sign in on new device.",
                            fontSize = 14.sp
                        )
                        TextButton(onClick = { /* Handle Learn More click */ }) {
                            Text(text = "Learn more", fontSize = 14.sp)
                        }
                    }
                }

                // Description 2
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.apscalculator), // Add the appropriate icon here
                        contentDescription = "Phone Icon",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Your phone number or Authenticator App helps us keep your account secure by adding an additional layer of verification. Your phone number also helps others, who already have your phone number, discover and connect with you. You can always decide how you want your phone number used.",
                            fontSize = 14.sp
                        )
                        TextButton(onClick = { /* Handle Learn More click */ }) {
                            Text(text = "Learn more", fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Set up button
                Button(
                    onClick = { /* Handle Set up click */ },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "Set up", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Note
                Text(
                    text = "Note: Turning this feature on will sign you out anywhere you're currently signed in and remove all your remembered devices.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
