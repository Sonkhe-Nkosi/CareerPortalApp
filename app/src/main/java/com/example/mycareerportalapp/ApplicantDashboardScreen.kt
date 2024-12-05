package com.example.mycareerportalapp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase

@Composable
fun ApplicantDashboardScreen(navController: NavController) {
    var approvedPosts by remember { mutableStateOf<List<BasePost>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedPostId by remember { mutableStateOf(true) }
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


    LaunchedEffect(Unit) {
        fetchAllApprovedPosts { posts ->
            approvedPosts = posts
            isLoading = false
        }
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

    Scaffold(
        topBar = { ApplicantDashboardTopBar(navController) },
        bottomBar = { ApplicantsBottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                if (approvedPosts.isEmpty()) {
                    Text(
                        "No approved posts available.",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(approvedPosts) { post ->
                            var showDetails by remember { mutableStateOf(false) }
                            PostCard(
                                title = post.title,
                                description = post.description,
                                navController = navController,
                                imageUri = post.imageUri
                            ) {
                                showDetails = !showDetails
                            }
                            if (showDetails) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Title: ${post.title}", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Description: ${post.description}", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    CustomButton(
                                        text = "Apply",
                                        onClick = {
                                            val emailIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf("example@gmail.com"))
                                                putExtra(android.content.Intent.EXTRA_SUBJECT, "Application for ${post.title}")
                                                putExtra(android.content.Intent.EXTRA_TEXT, "I am interested in applying for the ${post.title}.")
                                            }
                                            context.startActivity(emailIntent)
                                        },
                                        isLoading = isLoading

                                    )

                                }

                            }
                        }
                        item {
                            Text("", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            Text("Bursary Posts", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(bursaryPosts) { post ->
                            PosterItem(post = post, postType = "Bursary") {
                                selectedPostId = post.selectedPostId
                            }


                            if (selectedPostId == post.selectedPostId) {
                                AlertDialog(
                                    onDismissRequest = { selectedPostId = null == true },
                                    title = { Text("Apply for Post") },
                                    text = { Text("Would you like to apply for this post?") },
                                    confirmButton = {
                                        TextButton(onClick = {

                                            navController.navigate("ApplicationFormScreen/{postId}/{postTitle}")
                                            selectedPostId = null == true
                                        }) {
                                            Text("Yes")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = {

                                            navController.navigate("ApplicantDashboardScreen")
                                            selectedPostId = null == true
                                        }){
                                            Text("No")
                                        }
                                    }
                                )
                            }
                        }

                        // Job Posts
                        item {
                            Text("Job Posts", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(jobPosts) { post ->
                            PosterItem(post = post, postType = "Job") {
                                selectedPostId = post.selectedPostId
                            }


                            if (selectedPostId == post.selectedPostId) {
                                AlertDialog(
                                    onDismissRequest = { selectedPostId = true == true },
                                    title = { Text("Apply for Post") },
                                    text = { Text("Would you like to apply for this post?") },
                                    confirmButton = {
                                        TextButton(onClick = {

                                            navController.navigate("ApplicationFormScreen/{postId}/{postTitle}")
                                            selectedPostId = true == true
                                        }) {
                                            Text("Yes")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = {

                                            navController.navigate("ApplicantDashboardScreen")
                                            selectedPostId = true == true
                                        }){
                                            Text("No")
                                        }
                                    }
                                )
                            }
                        }

                        item {
                            Text("Internship Posts", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(internshipPosts) { post ->
                            PosterItem(post = post, postType = "Internship", onClick = {
                                selectedPostId = post.selectedPostId
                            })



                            if (selectedPostId == post.selectedPostId) {
                                AlertDialog(
                                    onDismissRequest = { selectedPostId = null == true },
                                    title = { Text("Apply for Post") },
                                    text = { Text("Would you like to apply for this post?") },
                                    confirmButton = {
                                        TextButton(onClick = {

                                            navController.navigate("ApplicationFormScreen/{postId}/{postTitle}")
                                            selectedPostId = true == true
                                        }) {
                                            Text("Yes")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = {

                                            navController.navigate("ApplicantDashboardScreen")
                                            selectedPostId = null == true
                                        }){
                                            Text("No")
                                        }
                                    }
                                )
                            }
                        }

                        item {
                            Text("Internship Posts", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            Text("Internship Posts", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(learnershipPosts) { post ->
                            PosterItem(post = post, postType = "Learnership", onClick = {
                                selectedPostId = post.selectedPostId
                            })


                            if (selectedPostId == post.selectedPostId) {
                                AlertDialog(
                                    onDismissRequest = { selectedPostId = null == true },
                                    title = { Text("Apply for Post") },
                                    text = { Text("Would you like to apply for this post?") },
                                    confirmButton = {
                                        TextButton(onClick = {

                                            navController.navigate("ApplicationFormScreen/{postId}/{postTitle}")
                                            selectedPostId = true
                                        }) {
                                            Text("Yes")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = {

                                            navController.navigate("ApplicantDashboardScreen")
                                            selectedPostId = null == true
                                        }){
                                            Text("No")
                                        }
                                    }
                                )
                            }
                        }

                    }

                }
            }
        }
    }
}

