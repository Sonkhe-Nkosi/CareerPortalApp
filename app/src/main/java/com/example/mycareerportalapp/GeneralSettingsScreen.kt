package com.example.mycareerportalapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
fun GeneralSettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "General Settings",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp, // Adjust the size to match the image
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle logo click if needed */ }) {
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
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Profile Information Section
                item {
                    SectionTitle(text = "Profile Information")
                    SettingsItemBlock(navController = navController, text = "Name, location, and education", navigateTo = "MyProfileScreen")
                    SettingsItemBlock(navController = navController, text = "Personal demographic information", navigateTo = "DemographicInformationScreen")


                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Divider(thickness = 8.dp, color = Color(0xFFD2B48C)) // Light brown section divider
                }

                // Display Section
                item {
                    SectionTitle(text = "Display")
                    SettingsItemBlock(navController = navController, text = "Dark mode", navigateTo = "DarkModeScreen")

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Divider(thickness = 8.dp, color = Color(0xFFD2B48C)) // Light brown section divider
                }

                // General Preferences Section
                item {
                    SectionTitle(text = "General Preferences")
                    SettingsItemBlock(navController = navController, text = "Language", navigateTo = "language_settings_screen")
                    SettingsItemBlock(navController = navController, text = "Content language", navigateTo = "content_language_screen")
                    SettingsItemBlock(navController = navController, text = "Autoplay videos", navigateTo = "autoplay_videos_screen", secondaryText = "On Mobile Data and Wi-Fi")
                    SettingsItemBlock(navController = navController, text = "Sound effects", navigateTo = "sound_effects_screen", secondaryText = "On")
                    SettingsItemBlock(navController = navController, text = "Showing profile photos", navigateTo = "profile_photos_screen", secondaryText = "All members")
                    SettingsItemBlock(navController = navController, text = "Feed preferences", navigateTo = "feed_preferences_screen")

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Divider(thickness = 8.dp, color = Color(0xFFD2B48C)) // Light brown section divider
                }

                // Syncing Options Section
                item {
                    SectionTitle(text = "Syncing Options")
                    SettingsItemBlock(navController = navController, text = "Sync contacts", navigateTo = "sync_contacts_screen")

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Divider(thickness = 8.dp, color = Color(0xFFD2B48C)) // Light brown section divider
                }

                // Subscriptions & Payments Section
                item {
                    SectionTitle(text = "Subscriptions & Payments")
                    SettingsItemBlock(navController = navController, text = "Upgrade for Free", navigateTo = "upgrade_screen")
                    SettingsItemBlock(navController = navController, text = "View purchase history", navigateTo = "purchase_history_screen")

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Divider(thickness = 8.dp, color = Color(0xFFD2B48C)) // Light brown section divider
                }

                // Partners & Services Section
                item {
                    SectionTitle(text = "Partners & Services")
                    SettingsItemBlock(navController = navController, text = "Microsoft", navigateTo = "microsoft_services_screen")

                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Divider(thickness = 8.dp, color = Color(0xFFD2B48C)) // Light brown section divider
                }

                // Account Management Section
                item {
                    SectionTitle(text = "Account Management")
                    SettingsItemBlock(navController = navController, text = "Hibernate account", navigateTo = "hibernate_account_screen")
                    SettingsItemBlock(navController = navController, text = "Close account", navigateTo = "close_account_screen")

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    )
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = 20.sp, // Increase font size
            fontWeight = FontWeight.Bold, // Make the sub-heading bold
            color = Color.Black
        ),
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun SettingsItemBlock(
    navController: NavController,
    text: String,
    navigateTo: String,
    secondaryText: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0)) // Light grey background color
            .padding(8.dp) // Add some padding inside the block
    ) {
        Column {
            SettingsItem(
                text = text,
                secondaryText = secondaryText,
                onClick = { navController.navigate(navigateTo) }
            )
            Divider(color = Color(0xFFD2B48C)) // Light brown divider
        }
    }
}

@Composable
fun SettingsItem(
    text: String,
    secondaryText: String? = null,
    onClick: () -> Unit // Add onClick function to handle navigation
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Make the entire row clickable
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text, style = MaterialTheme.typography.bodyLarge)
            if (secondaryText != null) {
                Text(
                    text = secondaryText,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }
        Icon(
            painter = painterResource(id = R.drawable.right_arrow), // Replace with your actual drawable resource
            contentDescription = "Arrow",
            tint = Color.Gray,
            modifier = Modifier.size(16.dp) // Reduce the size of the arrow icon
        )
    }
}
