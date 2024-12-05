package com.example.mycareerportalapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(navController: NavController, email: String,
               currentUserId: String,
               contactUserId: String) {
    var userRole by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var chats by remember { mutableStateOf<List<ChatItem>>(emptyList()) }

    var messageInput by remember { mutableStateOf(TextFieldValue()) }
    val messages = remember { mutableStateListOf<Message>() }

    val chatId = if (currentUserId < contactUserId) {
        "$currentUserId-$contactUserId"
    } else {
        "$contactUserId-$currentUserId"
    }

    fun sendMessage() {
        val text = messageInput.text.trim()
        if (text.isNotEmpty()) {
            val message = Message(
                senderId = currentUserId,
                receiverId = contactUserId,
                messageText = text,
                timestamp = System.currentTimeMillis()
            )
            messageInput = TextFieldValue() // Clear input field
            saveMessageToRealtimeDatabase(chatId, message) // Save to Firebase
            sendFcmNotification(contactUserId, text) // Send FCM notification
        }
    }
    LaunchedEffect(email) {
        fetchUserRole(
            email = email,
            onRoleFetched = { role -> userRole = role },
            onError = { error -> errorMessage = error }
        )
        fetchMessagesFromRealtimeDatabase(chatId, messages)
        fetchChats { fetchedChats -> chats = fetchedChats }
    }

    errorMessage?.let {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text(text = "Error") },
            text = { Text(text = it) },
            confirmButton = {
                TextButton(onClick = { errorMessage = null }) {
                    Text(text = "OK")
                }
            }
        )
    }

    Scaffold(
        topBar = { ChatTopBar(navController) }

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
                        text = "${message.senderName} -${message.messageText} - ${formatTimestamp(message.timestamp)}",
                        color = if (message.senderId == currentUserId) Color.Blue else Color.Gray,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}

fun fetchMessagesFromRealtimeDatabase(chatId: String, messages: SnapshotStateList<Message>) {
    val db = FirebaseDatabase.getInstance().reference
    val messageRef = db.child("messages").child(chatId) // Fetch only messages for this chatId

    messageRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            messages.clear() // Clear previous messages to avoid duplicates
            for (child in snapshot.children) {
                val message = child.getValue(Message::class.java)
                if (message != null) {
                    messages.add(message)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error if needed
        }
    })
}

fun saveMessageToRealtimeDatabase(chatId: String, message: Message) {
    val db = FirebaseDatabase.getInstance().reference
    val messageRef = db.child("messages").child(chatId).push() // Store under chatId

    messageRef.setValue(message)
        .addOnSuccessListener {
            // Log success if needed
        }
        .addOnFailureListener {
            // Handle failure
        }
}


// Helper function to send FCM notification
fun sendFcmNotification(receiverId: String, messageText: String) {
    // Send an FCM notification to receiverId with messageText
    // FCM setup and messaging logic go here
}

// Helper function to format timestamp
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Chat Room") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0, 52, 120, 255),
            titleContentColor = Color.White
        )
    )
}

@Composable
fun UserList(chats: List<ChatItem>, onChatClick: (ChatItem) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(chats) { chat ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onChatClick(chat) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

fun fetchUserRole(
    email: String,
    onRoleFetched: (String?) -> Unit,
    onError: (String) -> Unit
) {
    val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("UserList")

    userRef.get()
        .addOnSuccessListener { snapshot ->
            var foundRole: String? = null
            snapshot.children.forEach { roleSnapshot ->
                roleSnapshot.children.forEach { userSnapshot ->
                    val userEmail = userSnapshot.child("email").getValue(String::class.java)
                    if (userEmail == email) {
                        foundRole = roleSnapshot.key
                        return@forEach
                    }
                }
            }
            onRoleFetched(foundRole)
        }
        .addOnFailureListener { exception ->
            Log.e("FetchUserRole", "Error fetching user role: ${exception.message}")
            onError("Failed to retrieve user data.")
        }
}

fun fetchChats(onChatsFetched: (List<ChatItem>) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("chats")
        .get()
        .addOnSuccessListener { result ->
            val chatList = mutableListOf<ChatItem>()
            for (document in result) {
                val chat = document.toObject(ChatItem::class.java)
                chatList.add(chat)
            }
            onChatsFetched(chatList)
        }
        .addOnFailureListener { e ->
            Log.e("FetchChats", "Error fetching chats: ${e.message}")
        }
}

