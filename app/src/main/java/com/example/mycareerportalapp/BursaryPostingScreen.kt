package com.example.mycareerportalapp

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import java.util.*

@Composable
fun BursaryPostingScreen(navController: NavController) {
    var bursaryTitle by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var bursaryDescription by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isImagePickerEnabled by remember { mutableStateOf(false) }
    var isEmailPickerEnabled by remember { mutableStateOf(false) }
    var isUrlPickerEnabled by remember { mutableStateOf(false) }// Checkbox state
    val context = LocalContext.current
    val storageRef = Firebase.storage.reference.child("Images/${UUID.randomUUID()}.jpg")

    // Launcher for picking images
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
        } else {
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Post a Bursary",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Title Input Field
        OutlinedTextField(
            value = bursaryTitle,
            onValueChange = { bursaryTitle = it },
            label = { Text("Bursary Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Description Input Field
        OutlinedTextField(
            value = bursaryDescription,
            onValueChange = { bursaryDescription = it },
            label = { Text("Bursary Description") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )
        if (isUrlPickerEnabled) {
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Url goes here") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        if (isEmailPickerEnabled) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("enter email here") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        // Image Picker Checkbox
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isImagePickerEnabled,
                onCheckedChange = { isImagePickerEnabled = it }
            )
            Text("Include an image with this post", style = MaterialTheme.typography.bodyMedium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isUrlPickerEnabled,
                onCheckedChange = { isUrlPickerEnabled = it }
            )
            Text("Include a Url or link address with this post", style = MaterialTheme.typography.bodyMedium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isEmailPickerEnabled,
                onCheckedChange = { isEmailPickerEnabled = it }
            )
            Text("Include an email to apply for this post", style = MaterialTheme.typography.bodyMedium)
        }

        // Image Picker Button (conditionally visible)
        if (isImagePickerEnabled) {
            CustomButton(
                text = if (isLoading) "Uploading..." else "Pick an Image",
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading
            )
        }

        // Submit Button
        CustomButton(
            text = if (isLoading) "Posting..." else "Submit Post",
            onClick = {
                if (bursaryTitle.isNotEmpty() && bursaryDescription.isNotEmpty() &&
                    (!isImagePickerEnabled || imageUri != null)) {
                    isLoading = true
                    uploadBursaryPost(
                        bursaryTitle = bursaryTitle,
                        bursaryDescription = bursaryDescription,
                        imageUri = if (isImagePickerEnabled) imageUri else null,
                        url = url,
                        email = email,
                        storageRef = storageRef,
                        navController = navController,
                        context = context,
                        onCompletion = { isLoading = false }
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Please fill out all fields${if (isImagePickerEnabled) " and pick an image" else ""}.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading
        )
    }
}


fun uploadBursaryPost(
    bursaryTitle: String,
    bursaryDescription: String,
    email: String,
    url: String,
    imageUri: Uri?,
    storageRef: StorageReference,
    navController: NavController,
    context: Context,
    onCompletion: () -> Unit
) {
    if (imageUri != null) {
        // Upload the image to Firebase Storage
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveBursaryToDatabase(
                        title = bursaryTitle,
                        description = bursaryDescription,
                        imageUri = downloadUrl.toString(),
                        url = url,
                        email = email,
                        navController = navController,
                        context = context,
                        onCompletion = onCompletion
                    )
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to retrieve image URL.", Toast.LENGTH_SHORT).show()
                    onCompletion()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Image upload failed.", Toast.LENGTH_SHORT).show()
                onCompletion()
            }
    } else {
        // Directly save post without an image
        saveBursaryToDatabase(
            title = bursaryTitle,
            description = bursaryDescription,
            imageUri = null,
            email = email,
            url = url,
            navController = navController,
            context = context,
            onCompletion = onCompletion
        )
    }
}


fun saveBursaryToDatabase(
    title: String,
    email: String,
    url: String,
    description: String,
    imageUri: String?,
    navController: NavController,
    context: Context,
    onCompletion: () -> Unit
) {
    val database = FirebaseDatabase.getInstance("https://my-career-portal-app-default-rtdb.firebaseio.com/")
    val pendingRef = database.getReference("pendingBursaryPosts")

    val newPostRef = pendingRef.push()
    val postId = newPostRef.key ?: UUID.randomUUID().toString()

    val bursaryPost = imageUri?.let {
        BursaryPost(
            postId = postId,
            title = title,
            description = description,
            imageUri = it,
            email = email,
            url = url,
            approved = false
        )
    }

    newPostRef.setValue(bursaryPost)
        .addOnSuccessListener {
            Toast.makeText(context, "Bursary posted successfully!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to post bursary.", Toast.LENGTH_SHORT).show()
        }
        .addOnCompleteListener { onCompletion() }
}
