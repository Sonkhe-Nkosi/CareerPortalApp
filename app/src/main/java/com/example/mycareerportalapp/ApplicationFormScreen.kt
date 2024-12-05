package com.example.mycareerportalapp

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

@Composable
fun ApplicationFormScreen(
    navController: NavController,
    postId: String,
    postTitle: String
) {
    var fullName by remember { mutableStateOf("") }
    val applicationStatus by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var resumeUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var postReference by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var posterName by remember { mutableStateOf("") }

    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().getReference("pendingApplications")
    val storage = FirebaseStorage.getInstance().reference.child("resumes")

    // File picker launcher
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            resumeUri = uri
            Toast.makeText(context, "Resume selected: ${uri.lastPathSegment}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Apply here",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = fullName.isBlank()
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
        OutlinedTextField(
            value = postReference,
            onValueChange = { postReference = it },
            label = { Text("Post Reference") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = summary,
            onValueChange = { summary = it },
            label = { Text("Application Summary") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = posterName,
            onValueChange = { posterName = it },
            label = { Text("Poster Name") },
            modifier = Modifier.fillMaxWidth()
        )

        CustomButton(
            onClick = { launcher.launch("application/pdf") },
            text = if (resumeUri == null) "Upload Resume" else "Change Resume"
        )

        resumeUri?.let {
            Text("Selected Resume: ${it.lastPathSegment}", style = MaterialTheme.typography.bodySmall)
        }

        CustomButton(
            onClick = {
                // Validation checks
                if (fullName.isBlank()) {
                    Toast.makeText(context, "Name is required", Toast.LENGTH_SHORT).show()
                    return@CustomButton
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                    return@CustomButton
                }
                if (resumeUri == null) {
                    Toast.makeText(context, "Please upload a resume.", Toast.LENGTH_SHORT).show()
                    return@CustomButton
                }

                // Start loading indicator
                isLoading = true

                // Upload the resume file to Firebase Storage
                val resumeRef = storage.child("resumes/${resumeUri!!.lastPathSegment}")
                val uploadTask = resumeRef.putFile(resumeUri!!)
                uploadTask.addOnSuccessListener {
                    // Get the URL of the uploaded resume
                    resumeRef.downloadUrl.addOnSuccessListener { resumeUrl ->
                        // Prepare application data to save in Realtime Database
                        val applicationData = mapOf(
                            "posterName" to posterName,
                            "postReference" to postReference,
                            "summary" to summary,
                            "fullName" to fullName,
                            "email" to email,
                            "applicationStatus" to "Pending",
                            "resumeUrl" to resumeUrl.toString()
                        )

                        // Get the current count of applications to determine the next index
                        val applicationsRef = database.child("pendingApplications")

                        applicationsRef.get().addOnSuccessListener {
                            val applicationKey = "pendingApplication"

                            // Save the application under the generated key (e.g., pendingApplication1)
                            applicationsRef.child(applicationKey).setValue(applicationData)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Application submitted successfully!", Toast.LENGTH_LONG).show()
                                    isLoading = false
                                    navController.popBackStack() // Navigate back after submission
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed to submit application.", Toast.LENGTH_LONG).show()
                                    isLoading = false
                                }
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed to get application count.", Toast.LENGTH_LONG).show()
                            isLoading = false
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to upload resume.", Toast.LENGTH_LONG).show()
                    isLoading = false
                }
            },
            text = if (isLoading) "" else "Submit Application",
            enabled = !isLoading
        )


        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp),
                strokeWidth = 2.dp
            )
        }
    }
}