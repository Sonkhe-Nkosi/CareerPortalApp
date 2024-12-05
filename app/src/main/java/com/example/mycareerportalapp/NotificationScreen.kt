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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    Scaffold(
        topBar = { GenericTopBar(navController, "Notifications") },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // The Banner at the top
                item {
                    BannerNotification()
                }

                // "Notifications you receive" header
                item {
                    Text(
                        text = "Notifications you receive",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // List of notification settings
                val notificationItems = listOf(
                    "Searching for a job",
                    "Hiring someone",
                    "Connecting with others",
                    "Network catch-up updates",
                    "Posting and commenting",
                    "Messaging",
                    "Groups",
                    "Pages",
                    "Attending events",
                    "News and reports",
                    "Updating your profile"
                )

                items(notificationItems.size) { index ->
                    NotificationListItem(
                        text = notificationItems[index],
                        onClick = {
                            // Handle click event
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun BannerNotification() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Your notification settings are regrouped and simplified",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Your previous setting choices aren’t changed.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
            }
            TextButton(onClick = { /* Handle Learn More Click */ }) {
                Text(text = "Learn More")
            }
        }
    }
}

@Composable
fun NotificationListItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text)
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
    Divider()
}
