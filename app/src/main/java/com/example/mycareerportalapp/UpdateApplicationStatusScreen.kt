package com.example.mycareerportalapp
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.*

@Composable
fun UpdateApplicationStatusScreen(
    navController: NavController,
    applicantId: String
) {
    val context = LocalContext.current
    var applicationDetails by remember { mutableStateOf<Map<String, String>?>(null) }
    var status by remember { mutableStateOf("On Review") }

    // Fetch application details from Firebase Realtime Database
    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance()
        val applicationRef = database.getReference("applications").child(applicantId)

        applicationRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                applicationDetails = snapshot.value as? Map<String, String>
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load application details", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Update Application Status", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Display application details
        applicationDetails?.let { details ->
            Text("Name: ${details["name"]}", style = MaterialTheme.typography.bodyMedium)
            Text("Email: ${details["email"]}", style = MaterialTheme.typography.bodyMedium)
            Text("Post Title: ${details["postTitle"]}", style = MaterialTheme.typography.bodyMedium)
            Text("Summary: ${details["summary"]}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                downloadResume(context, details["resumeUrl"] ?: "")
            }) {
                Text("Download Resume")
            }
        } ?: Text("Loading application details...")

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown or input field for status
        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                updateApplicationStatus(applicantId, status, context)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Status")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                createOffer(applicantId, context)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Offer")
        }
    }
}

private fun downloadResume(context: Context, resumeUrl: String) {
    if (resumeUrl.isNotBlank()) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resumeUrl))
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Resume URL is not available", Toast.LENGTH_SHORT).show()
    }
}

private fun updateApplicationStatus(applicantId: String, status: String, context: Context) {
    val database = FirebaseDatabase.getInstance()
    val applicationRef = database.getReference("applications").child(applicantId)

    applicationRef.child("status").setValue(status)
        .addOnSuccessListener {
            Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show()
        }
}

private fun createOffer(applicantId: String, context: Context) {
    // Logic to create an offer for the applicant
    Toast.makeText(context, "Offer creation logic goes here", Toast.LENGTH_SHORT).show()
}
