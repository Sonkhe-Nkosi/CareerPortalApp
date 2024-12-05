package com.example.mycareerportalapp

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPostScreen(
    navController: NavController,
    postId: String,
    postTitle: String,
    postDescription: String,
    postImageUri: String
) {
    val context = LocalContext.current

    // Apply email functionality
    fun sendApplicationEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Application for $postTitle")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm interested in applying for the $postTitle position. Here is my cover letter...")
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
        } catch (e: Exception) {
            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(postTitle) },  // Set the title to the post title
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Display post image
            AsyncImage(
                model = postImageUri,
                contentDescription = postTitle,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display post description
            Text(
                text = postDescription,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Apply button
            Button(
                onClick = { sendApplicationEmail() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Apply")
            }
        }
    }
}

fun fetchPostById(postId: String, onPostFetched: (BasePost?) -> Unit) {
    val database = FirebaseDatabase.getInstance(FIREBASE_URL)
    val dbRef = database.getReference("approvedPosts").child(postId)

    dbRef.get().addOnSuccessListener { snapshot ->
        if (snapshot.exists()) {
            val title = snapshot.child("title").getValue(String::class.java) ?: ""
            val description = snapshot.child("description").getValue(String::class.java) ?: ""
            val imageUri = snapshot.child("imageUri").getValue(String::class.java) ?: ""

            val post = BasePost(postId, title, description, imageUri)
            onPostFetched(post)
        } else {
            onPostFetched(null)
        }
    }.addOnFailureListener { error ->
        error.printStackTrace()
        onPostFetched(null)
    }
}
