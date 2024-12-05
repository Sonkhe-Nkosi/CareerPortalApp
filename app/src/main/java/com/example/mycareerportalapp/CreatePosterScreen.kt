package com.example.mycareerportalapp

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePosterScreen(navController: NavController, employerId: String) {
    val context = LocalContext.current
    var postReference by remember { mutableStateOf("") }
    var selectedPostType by remember { mutableStateOf<PostType?>(null) }
    val isLoading by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var bursaryAmount by remember { mutableStateOf("") }
    var applicationDeadline by remember { mutableStateOf("") }
    var jobLocation by remember { mutableStateOf("") }
    var salaryRange by remember { mutableStateOf("") }
    var internshipDuration by remember { mutableStateOf("") }
    var learnershipDuration by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isImagePickerEnabled by remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Create a New Poster", style = MaterialTheme.typography.titleMedium)

        // Dropdown for selecting post type
        Text("Select Post Type", style = MaterialTheme.typography.bodyMedium)
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            TextField(
                value = selectedPostType?.nodeName ?: "Select Post Type",
                onValueChange = {},
                readOnly = true,
                label = { Text("Post Type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                PostType.values().forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.nodeName) },
                        onClick = {
                            selectedPostType = type
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Input fields for title, description, etc.
        OutlinedTextField(
            value = postReference,
            onValueChange = { postReference = it },
            label = { Text("Post Reference") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        // Conditional fields for different post types
        if (selectedPostType == PostType.BURSARY) {
            TextField(
                value = bursaryAmount,
                onValueChange = { bursaryAmount = it },
                label = { Text("Bursary Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = applicationDeadline,
                onValueChange = { applicationDeadline = it },
                label = { Text("Application Deadline") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (selectedPostType == PostType.LEARNERSHIP) {
            TextField(
                value = learnershipDuration,
                onValueChange = { learnershipDuration = it },
                label = { Text("Learnership Duration") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = applicationDeadline,
                onValueChange = { applicationDeadline = it },
                label = { Text("Application Deadline") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (selectedPostType == PostType.JOB) {
            TextField(
                value = jobLocation,
                onValueChange = { jobLocation = it },
                label = { Text("Job Location") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Additional fields for Internship Post
        if (selectedPostType == PostType.INTERNSHIP) {
            TextField(
                value = internshipDuration,
                onValueChange = { internshipDuration = it },
                label = { Text("Internship Duration") },
                modifier = Modifier.fillMaxWidth()
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

        // Image Picker Button
        if (isImagePickerEnabled) {
            CustomButton(
                text = "Pick an Image",
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isLoading) {
            CircularProgressIndicator(color = Color.Blue)
        } else {
            CustomButton(
            text = "Save Poster",
            onClick = {
                // Validate if a post type and title are selected
                if (selectedPostType != null && title.isNotBlank()) {
                    // Upload image and create post
                    imageUri?.let { uri ->
                        uploadImage(uri, context) { downloadUrl ->
                            val post = when (selectedPostType) {
                                PostType.BURSARY -> EmployerBursaryPost(
                                    postId = generatePostId(),
                                    postReference = postReference,
                                    employerId = employerId,
                                    title = title,
                                    imageUri = downloadUrl.toString(),
                                    description = description,
                                    bursaryAmount = bursaryAmount,
                                    applicationDeadline = applicationDeadline,
                                    approved = false
                                )
                                PostType.LEARNERSHIP -> EmployerLearnershipPost(
                                    postId = generatePostId(),
                                    postReference = postReference,
                                    employerId = employerId,
                                    learnershipTitle = title,
                                    learnershipDescription = description,
                                    learnershipDuration = learnershipDuration,
                                    applicationDeadline = applicationDeadline,
                                    imageUri = downloadUrl.toString(),
                                    approved = false
                                )
                                PostType.JOB -> EmployerJobPost(
                                    postId = generatePostId(),
                                    postReference = postReference,
                                    employerId = employerId,
                                    jobTitle = title,
                                    jobDescription = description,
                                    jobLocation = jobLocation,
                                    salaryRange = salaryRange,
                                    applicationDeadline = applicationDeadline,
                                    imageUri = downloadUrl.toString(),
                                    approved = false
                                )
                                PostType.INTERNSHIP -> EmployerInternshipPost(
                                    postId = generatePostId(),
                                    postReference = postReference,
                                    employerId = employerId,
                                    internshipTitle = title,
                                    internshipDescription = description,
                                    internshipDuration = internshipDuration,
                                    applicationDeadline = applicationDeadline,
                                    imageUri = downloadUrl.toString(),
                                    approved = false
                                )
                                else -> null
                            }

                            post?.let {
                                savePostToFirebase(it, navController, context)
                            }
                        }
                    } ?: run {
                        Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
}

private fun uploadImage(imageUri: Uri, context: Context, onSuccess: (Uri) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
    imageRef.putFile(imageUri)
        .addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri)
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
        }
}

private fun savePostToFirebase(post: EmployerBasePost, navController: NavController, context: Context) {
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("employerPosts")
    val postRef = when (post) {
        is EmployerBursaryPost -> database.child("approvedBursaryPosts").push()
        is EmployerLearnershipPost -> database.child("approvedLearnershipPosts").push()
        is EmployerJobPost -> database.child("approvedJobPosts").push()
        is EmployerInternshipPost -> database.child("approvedInternshipPosts").push()
        else -> return
    }

    postRef.setValue(post).addOnSuccessListener {
        Toast.makeText(context, "Post saved successfully!", Toast.LENGTH_SHORT).show()
        navController.popBackStack()
    }.addOnFailureListener {
        Toast.makeText(context, "Failed to save post.", Toast.LENGTH_SHORT).show()
    }
}
fun generatePostId(): String {
    return UUID.randomUUID().toString()
}
fun generateEmployerId(): String {
    return UUID.randomUUID().toString()
}