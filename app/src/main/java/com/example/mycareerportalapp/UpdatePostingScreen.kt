package com.example.mycareerportalapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import java.util.*

@Composable
fun UpdatePostingScreen(navController: NavController) {
    var updateTitle by remember { mutableStateOf("") }
    var updateDescription by remember { mutableStateOf("") }
    val context = LocalContext.current
    val nodeName = "YourNodeName"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Post an Update",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Title Input Field
        OutlinedTextField(
            value = updateTitle,
            onValueChange = { updateTitle = it },
            label = { Text("Update Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description Input Field
        OutlinedTextField(
            value = updateDescription,
            onValueChange = { updateDescription = it },
            label = { Text("Update Description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        CustomButton(
            text = "Submit Post",
            onClick = {
                if (updateTitle.isNotEmpty() && updateDescription.isNotEmpty()) {
                    uploadUpdatePost(
                        updateTitle = updateTitle,
                        updateDescription = updateDescription,
                        nodeName =  nodeName,
                        context = context,
                        navController = navController
                    )
                } else {
                    Toast.makeText(context, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Function to upload the update post to Firebase
fun uploadUpdatePost(
    updateTitle: String,
    updateDescription: String,
    nodeName: String,
    context: Context,
    navController: NavController
) {
    // Store the post in the pendingUpdatePosts node
    val database = FirebaseDatabase.getInstance("https://my-career-portal-app-default-rtdb.firebaseio.com/")
    val pendingRef = database.getReference("pendingUpdatePosts")

    val updatePost = UpdatePost(
        updateTitle = updateTitle,
        updateDescription = updateDescription,
        approved = false
    )

    val postId = pendingRef.push().key ?: UUID.randomUUID().toString()
    pendingRef.child(postId).setValue(updatePost)
        .addOnSuccessListener {
            Toast.makeText(context, "Update posted successfully!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to post update.", Toast.LENGTH_SHORT).show()
        }
}
