package com.example.mycareerportalapp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavController, loggedInUserId: String) {
    val context = LocalContext.current
    val contactsList = remember { mutableStateListOf<UserList>() }


    LaunchedEffect(Unit) {
        fetchContactsFromRealtimeDatabase(loggedInUserId) { contacts ->
            contactsList.clear()
            contactsList.addAll(contacts)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF043776),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(contactsList) { contact ->
                ContactItemRow(contact, navController, loggedInUserId)
            }
        }
    }
}

@Composable
fun ContactItemRow(contact: UserList, navController: NavController, loggedInUserId: String) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                createOrRetrieveChat(contact.cellphoneNumber, loggedInUserId) { chatId ->
                    navController.navigate("ConversationScreen/{contactName}/{contactProfileImage}")
                    Toast.makeText(context, "Opening chat with ${contact.fullName}", Toast.LENGTH_SHORT).show()
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image with placeholder
        val profileImagePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(contact.profileImage ?: R.drawable.default_profile) // Default profile image
                .size(Size.ORIGINAL)
                .build()
        )

        Image(
            painter = profileImagePainter,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = contact.fullName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontSize = 18.sp
            )
            Text(
                text = contact.cellphoneNumber,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

fun fetchContactsFromRealtimeDatabase(loggedInUserId: String, onContactsFetched: (List<UserList>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val userListRef = database.getReference("UserList")
    val contacts = mutableListOf<UserList>()

    userListRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach { roleSnapshot ->
                roleSnapshot.children.forEach { userSnapshot ->
                    val user = userSnapshot.getValue(UserList::class.java)
                    if (user != null && user.userId != loggedInUserId) {
                        contacts.add(user)
                    }
                }
            }
            onContactsFetched(contacts)
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error case
        }
    })
}

fun createOrRetrieveChat(contactPhoneNumber: String, loggedInUserId: String, onChatCreated: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val chatsRef = db.collection("chats")

    chatsRef
        .whereArrayContains("participants", contactPhoneNumber)
        .whereArrayContains("participants", loggedInUserId)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val chatId = querySnapshot.documents.first().id
                onChatCreated(chatId)
            } else {
                val newChat = hashMapOf(
                    "participants" to listOf(loggedInUserId, contactPhoneNumber),
                    "lastMessage" to "",
                    "timestamp" to System.currentTimeMillis()
                )
                chatsRef.add(newChat)
                    .addOnSuccessListener { documentReference ->
                        onChatCreated(documentReference.id)
                    }
            }
        }
        .addOnFailureListener {
            // Handle error case here
        }
}

data class UserList(
    val userId: String = "",
    val cellphoneNumber: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "",
    val profileImage: String? = null,
    val status: String? = "Available"
)
