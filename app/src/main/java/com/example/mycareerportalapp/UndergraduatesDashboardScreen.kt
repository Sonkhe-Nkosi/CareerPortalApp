package com.example.mycareerportalapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase

@Composable
fun UndergraduatesDashboardScreen(navController: NavController) {
    var approvedPosts by remember { mutableStateOf<List<BasePost>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch all approved posts on screen load
    LaunchedEffect(Unit) {
        fetchAllApprovedPosts { posts ->
            approvedPosts = posts
            isLoading = false
        }
    }

    Scaffold(
        topBar = { UndergraduatesDashboardScreenTopBar(navController) },
        bottomBar = { BottomNavigationBar(navController) }
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
            } else if (approvedPosts.isEmpty()) {
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
                        PostCard(
                            title = post.title,
                            description = post.description,
                            navController = navController,
                            imageUri = post.imageUri
                        ) {
                            navController.navigate("ViewPostScreen/{postId}/{postTitle}/{postDescription}/{postImageUri}")

                        }
                    }
                }
            }
        }
    }
}


// Function to fetch all approved posts from Firebase
fun fetchAllApprovedPosts(onPostsFetched: (List<BasePost>) -> Unit) {
    val database = FirebaseDatabase.getInstance(FIREBASE_URL)
    val approvedPosts = mutableListOf<BasePost>()

    // Fetch approved generic posts
    val genericRef = database.getReference("approvedgenericPostss")
    genericRef.get().addOnSuccessListener { genericDataSnapshot ->
        for (postSnapshot in genericDataSnapshot.children) {
            val approved = postSnapshot.child("approved").getValue(Boolean::class.java) ?: false
            if (approved) {
                val title = postSnapshot.child("title").getValue(String::class.java) ?: ""
                val description = postSnapshot.child("description").getValue(String::class.java) ?: ""
                val imageUri = postSnapshot.child("imageUri").getValue(String::class.java) ?: ""
                val postId = postSnapshot.child("postId").getValue(String::class.java) ?: ""

                approvedPosts.add(BasePost(postId, title, description, imageUri))
            }
        }
        // After fetching generic posts, fetch bursary posts
        fetchApprovedBursaryPosts { bursaryPosts ->
            approvedPosts.addAll(bursaryPosts)
            onPostsFetched(approvedPosts)
        }
    }.addOnFailureListener {
        // In case of failure in fetching generic posts, try fetching bursary posts
        fetchApprovedBursaryPosts { bursaryPosts ->
            approvedPosts.addAll(bursaryPosts)
            onPostsFetched(approvedPosts)
        }
    }
}

// Function to fetch all approved bursary posts from Firebase
fun fetchApprovedBursaryPosts(onPostsFetched: (List<BasePost>) -> Unit) {
    val database = FirebaseDatabase.getInstance(FIREBASE_URL)
    val approvedPosts = mutableListOf<BasePost>()

    val dbRef = database.getReference("approvedbursaryPostss")
    dbRef.get().addOnSuccessListener { dataSnapshot ->
        for (postSnapshot in dataSnapshot.children) {
            val approved = postSnapshot.child("approved").getValue(Boolean::class.java) ?: false
            if (approved) {
                val title = postSnapshot.child("title").getValue(String::class.java) ?: ""
                val description = postSnapshot.child("description").getValue(String::class.java) ?: ""
                val imageUri = postSnapshot.child("imageUri").getValue(String::class.java) ?: ""
                val postId = postSnapshot.child("postId").getValue(String::class.java) ?: ""

                approvedPosts.add(BasePost(postId, title, description, imageUri))
            }
        }
        onPostsFetched(approvedPosts)
    }.addOnFailureListener {
        onPostsFetched(approvedPosts)  // Return empty list if there's an error
    }
}
