package com.example.mycareerportalapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.database.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterHistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().getReference("employerPosts")
    val bursaryPostsRef = database.child("approvedBursaryPosts")
    val jobPostsRef = database.child("approvedJobPosts")
    val internshipPostsRef = database.child("approvedInternshipPosts")
    val learnershipPostsRef = database.child("approvedLearnershipPosts")

    var bursaryPosts by remember { mutableStateOf<List<EmployerBursaryPost>>(emptyList()) }
    var jobPosts by remember { mutableStateOf<List<EmployerJobPost>>(emptyList()) }
    var internshipPosts by remember { mutableStateOf<List<EmployerInternshipPost>>(emptyList()) }
    var learnershipPosts by remember { mutableStateOf<List<EmployerLearnershipPost>>(emptyList()) }

    // Fetch posts from Firebase
    LaunchedEffect(Unit) {
        bursaryPostsRef.get().addOnSuccessListener { snapshot ->
            bursaryPosts = snapshot.children.mapNotNull { it.getValue(EmployerBursaryPost::class.java) }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch Bursary Posts", Toast.LENGTH_SHORT).show()
        }

        jobPostsRef.get().addOnSuccessListener { snapshot ->
            jobPosts = snapshot.children.mapNotNull { it.getValue(EmployerJobPost::class.java) }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch Job Posts", Toast.LENGTH_SHORT).show()
        }

        internshipPostsRef.get().addOnSuccessListener { snapshot ->
            internshipPosts = snapshot.children.mapNotNull { it.getValue(EmployerInternshipPost::class.java) }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch Internship Posts", Toast.LENGTH_SHORT).show()
        }

        learnershipPostsRef.get().addOnSuccessListener { snapshot ->
            learnershipPosts = snapshot.children.mapNotNull { it.getValue(EmployerLearnershipPost::class.java) }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch Learnership Posts", Toast.LENGTH_SHORT).show()
        }
    }

    // Scaffold to add top bar with back button
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Poster History", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text(
                            text = "<",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Bursary Posts
                item {
                    Text("Bursary Posts", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(bursaryPosts) { post ->
                    PosterItem(post = post, postType = "Bursary") {}
                }

                // Job Posts
                item {
                    Text("Job Posts", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(jobPosts) { post ->
                    PosterItem(post = post, postType = "Job") {}
                }

                // Internship Posts
                item {
                    Text("Internship Posts", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(internshipPosts) { post ->
                    PosterItem(post = post, postType = "Internship") {}
                }

                // Learnership Posts
                item {
                    Text("Learnership Posts", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(learnershipPosts) { post ->
                    PosterItem(post = post, postType = "Learnership") {}
                }
            }
        }
    )
}

@Composable
fun PosterItem(post: EmployerBasePost, postType: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Display image using Coil
            val painter = rememberImagePainter(data = post.imageUri)
            Image(
                painter = painter,
                contentDescription = null, // Use a suitable content description for accessibility
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Adjust the height as needed
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(post.title, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(post.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Display specific details based on the post type
            when (post) {
                is EmployerBursaryPost -> {
                    Text("Bursary Amount: ${post.bursaryAmount}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Application Deadline: ${post.applicationDeadline}", style = MaterialTheme.typography.bodySmall)
                }
                is EmployerLearnershipPost -> {
                    Text("Learnership Duration: ${post.learnershipDuration}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Application Deadline: ${post.applicationDeadline}", style = MaterialTheme.typography.bodySmall)
                }
                is EmployerJobPost -> {
                    Text("Job Location: ${post.jobLocation}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Salary Range: ${post.salaryRange}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Application Deadline: ${post.applicationDeadline}", style = MaterialTheme.typography.bodySmall)
                }
                is EmployerInternshipPost -> {
                    Text("Internship Duration: ${post.internshipDuration}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Application Deadline: ${post.applicationDeadline}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
