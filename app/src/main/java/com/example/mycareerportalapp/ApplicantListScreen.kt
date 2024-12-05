package com.example.mycareerportalapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantListScreen(navController: NavController) {
    val databaseRef = FirebaseDatabase.getInstance().getReference("pendingApplications")
    val evaluatedRef = FirebaseDatabase.getInstance().getReference("EvaluatedApplications")
    var applicants by remember { mutableStateOf(listOf<Applicant>()) }

    LaunchedEffect(Unit) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val appList = mutableListOf<Applicant>()
                for (appSnapshot in snapshot.children) {
                    val applicant = appSnapshot.getValue(Applicant::class.java)
                    if (applicant != null) {
                        appList.add(applicant)
                    }
                }
                applicants = appList
            }

            override fun onCancelled(error: DatabaseError) {
                // Log or handle errors
                error.toException().printStackTrace()
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Applicant List") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (applicants.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No applications available.")
                }
            } else {
                LazyColumn {
                    items(applicants) { applicant ->
                        ApplicantItem(
                            applicant = applicant,
                            updateStatus = { newStatus ->
                                moveApplication(
                                    applicant = applicant,
                                    newStatus = newStatus,
                                    databaseRef = databaseRef,
                                    evaluatedRef = evaluatedRef
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicantItem(applicant: Applicant, updateStatus: (String) -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${applicant.fullName}")
            Text(text = "Email: ${applicant.email}")
            Text(text = "Status: ${applicant.applicationStatus}")
            Text(text = "Post Reference: ${applicant.postReference}")
            Text(text = "Summary: ${applicant.summary}")
            Text(text = "Poster Name: ${applicant.posterName}")

            applicant.resumeUrl?.let { resumeUrl ->
                Text(
                    text = "Resume: View/Download",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        openResumeUrl(context, resumeUrl)
                    }
                )
            } ?: Text("Resume: Not attached")

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { updateStatus("Approved") }) {
                    Text("Approve")
                }
                Button(onClick = { updateStatus("Declined") }) {
                    Text("Decline")
                }
            }
        }
    }
}

fun openResumeUrl(context: android.content.Context, resumeUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resumeUrl))
    context.startActivity(intent)
}

fun moveApplication(
    applicant: Applicant,
    newStatus: String,
    databaseRef: DatabaseReference,
    evaluatedRef: DatabaseReference
) {
    val applicantId = applicant.applicantId ?: return

    // Determine the target sub-node based on the evaluation status
    val targetNode = if (newStatus == "Approved") {
        "ApprovedApplications"
    } else {
        "RejectedApplications"
    }

    // Update the application status
    val updatedApplicant = applicant.copy(applicationStatus = newStatus)

    // Store the application in the appropriate sub-node under EvaluatedApplications
    evaluatedRef.child(targetNode).child(applicantId).setValue(updatedApplicant)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Remove the application from the PendingApplications node only if the move is successful
                databaseRef.child(applicantId).removeValue()
            } else {
                // Log or handle errors
                task.exception?.printStackTrace()
            }
        }
}
