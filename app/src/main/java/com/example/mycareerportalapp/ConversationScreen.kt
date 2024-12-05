package com.example.mycareerportalapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <NavController> ConversationScreen(
    contactName: String,
    contactProfileImage: String?,
    navController: NavController,
    currentUserId: String,
    contactUserId: String
) {
    var messageInput by remember { mutableStateOf(TextFieldValue()) }
    val messages = remember { mutableStateListOf<Message>() }

    val chatId = if (currentUserId < contactUserId) {
        "$currentUserId-$contactUserId"
    } else {
        "$contactUserId-$currentUserId"
    }

    // Fetch messages from Firebase when screen is created
    LaunchedEffect(Unit) {
        fetchMessagesFromRealtimeDatabase(chatId, messages)
    }

    // Function to send a message


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat Room") },
                navigationIcon = {
                },

                actions = {

                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0, 52, 120, 255),
                    titleContentColor = Color.White
                )
            )
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter // Ensure messages are aligned at the top
        ) {
            // LazyColumn to display messages
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                reverseLayout = true // Optional: scroll position starts at the bottom
            ) {
                items(messages) { message ->
                    Text(
                        text = "${message.senderName}  - ${message.messageText} - ${formatTimestamp(message.timestamp)}",
                        color = if (message.senderId == currentUserId) Color.Blue else Color.Gray,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}
