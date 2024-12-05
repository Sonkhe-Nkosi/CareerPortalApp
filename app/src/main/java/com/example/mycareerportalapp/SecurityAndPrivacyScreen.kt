package com.example.mycareerportalapp

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityAndPrivacyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            // Copied TopAppBar from GeneralSettingsScreen and changed the title to "Change Password"
            TopAppBar(
                title = {
                    Text(
                        text = "Sign in & security",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp, // Adjust the size to match the image
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { // Handle back navigation
                        Icon(
                            painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
                            contentDescription = "Logo",
                            tint = Color.Unspecified // Keep the original colors of the logo
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // White background
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp) // Adjust padding to align with the image
            )
        },
        bottomBar = {
            // BottomNavigationBar from GeneralSettingsScreen
            BottomNavigationBar(navController)
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Account Access Section Header
                item {
                    Text(
                        text = "Account access",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // List of Security and Privacy settings
                val settingsItems = listOf(
                    "Email addresses" to "sibusisopeterson4@gmail.com",
                    "Phone numbers" to "",
                    "Passkeys" to "",
                    "Where you're signed in" to "",
                    "Devices that remember your password" to "",
                    "Two-step verification" to "Off",
                    "App lock" to ""
                )

                items(settingsItems.size) { index ->
                    val (title, detail) = settingsItems[index]
                    SecurityAndPrivacyListItem(
                        title = title,
                        detail = detail,
                        onClick = {
                            // Handle click event, e.g., navigate to respective screens
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun SecurityAndPrivacyListItem(title: String, detail: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            if (detail.isNotEmpty()) {
                Text(
                    text = detail,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
    Divider()
}
