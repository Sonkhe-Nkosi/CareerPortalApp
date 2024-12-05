package com.example.mycareerportalapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.database.FirebaseDatabase

const val FIREBASE_URL = "https://my-career-portal-app-default-rtdb.firebaseio.com/"

@Composable
fun AdminDashboardScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<BasePost?>(null) }
    var pendingPosts by remember { mutableStateOf<List<BasePost>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Fetch all pending posts on screen load
    LaunchedEffect(Unit) {
        fetchAllPendingPosts { posts ->
            pendingPosts = posts
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HeaderSection(navController)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (pendingPosts.isEmpty()) {
            Text("No pending posts available.", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(pendingPosts) { post ->
                    PostCard(
                        title = post.title,
                        description = post.description,
                        imageUri = post.imageUri,
                        navController = navController,
                        onClick = {
                            selectedPost = post
                            showDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showDialog && selectedPost != null) {
        ApproveDeclineDialog(
            post = selectedPost,
            onApprove = { post ->
                approvePost(post, post.nodeName, context) {
                    showDialog = false // Close the dialog
                }
            },
            onDecline = { post ->
                declinePost(post, post.nodeName, context) {
                    showDialog = false // Close the dialog
                }
            },
            navController = navController,
            onDismiss = { showDialog = false }
        )
    }
}

fun fetchAllPendingPosts(onPostsFetched: (List<BasePost>) -> Unit) {
    val database = FirebaseDatabase.getInstance(FIREBASE_URL)
    val pendingNodes = listOf(
        "pendingBursaryPosts" to BursaryPost::class.java,
        "pendingLearnershipPosts" to LearnershipPost::class.java,
        "pendingJobPosts" to JobPost::class.java,
        "pendingInternshipPosts" to InternshipPost::class.java,
        "pendingUpdatePosts" to UpdatePost::class.java
    )

    val pendingPosts = mutableListOf<BasePost>()
    val totalCategories = pendingNodes.size
    var completedCategories = 0

    pendingNodes.forEach { (node, postType) ->
        val dbRef = database.getReference(node)
        dbRef.get().addOnSuccessListener { dataSnapshot ->
            for (postSnapshot in dataSnapshot.children) {
                val post = postSnapshot.getValue(postType) as? BasePost // Cast to BasePost
                if (post != null) {
                    // Fetch the image URI if available
                    val imageUri = postSnapshot.child("imageUri").getValue(String::class.java) ?: ""
                    pendingPosts.add(post.toCopy(imageUri = imageUri, approved = true)) // Use toCopy method
                }
            }
            completedCategories++
            if (completedCategories == totalCategories) {
                onPostsFetched(pendingPosts)
            }
        }.addOnFailureListener {
            completedCategories++
            if (completedCategories == totalCategories) {
                onPostsFetched(pendingPosts)
            }
        }
    }
}


@Composable
fun HeaderSection(navController: NavController) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            val uri = "https://console.firebase.google.com/project/my-career-portal-app/database/my-career-portal-app-default-rtdb/data/~2F"
            context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri)))
        }) {
            Icon(painter = painterResource(id = R.drawable.firebaseicon), contentDescription = "Firebase Console")
        }

        Box {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(painter = painterResource(id = R.drawable.addbuttonicon), contentDescription = "Add Post")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(text = { Text("Bursary") }, onClick = { navController.navigate("BursaryPostingScreen") })
                DropdownMenuItem(text = { Text("Learnership") }, onClick = { navController.navigate("LearnershipPostingScreen") })
                DropdownMenuItem(text = { Text("Job") }, onClick = { navController.navigate("JobPostingScreen") })
                DropdownMenuItem(text = { Text("Internship") }, onClick = { navController.navigate("InternshipPostingScreen") })
                DropdownMenuItem(text = { Text("Updates") }, onClick = { navController.navigate("UpdatePostingScreen") })
            }
        }
    }
}

@Composable
fun PostCard(
    title: String,
    description: String,
    imageUri: String,
    navController: NavController,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            if (imageUri.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(imageUri),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(bottom = 8.dp)
                )
            }
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ApproveDeclineDialog(
    post: BasePost?,
    onApprove: (BasePost) -> Unit,
    onDecline: (BasePost) -> Unit,
    navController: NavController,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Approve or Decline Post") },
        text = { Text("Are you sure you want to approve or decline this post: ${post?.title}?") },
        confirmButton = {
            TextButton(onClick = { post?.let { onApprove(it) } }) {
                Text("Approve")
            }
        },
        dismissButton = {
            TextButton(onClick = { post?.let { onDecline(it) } }) {
                Text("Decline")
            }
        }
    )
}

fun approvePost(
    post: BasePost,
    nodeName: String,
    context: Context,
    onDismiss: () -> Unit // Parameter for dismissing the dialog
) {
    val database = FirebaseDatabase.getInstance(FIREBASE_URL)
    val approvedRef = database.getReference("approved${nodeName}s")
    val pendingRef = database.getReference("pending${nodeName}s")
    val postId = post.title.replace(" ", "_").lowercase()

    // Use the toCopy method instead of the copy method
    approvedRef.child(postId).setValue(post.toCopy(approved = true))
        .addOnSuccessListener {
            pendingRef.child(postId).removeValue().addOnCompleteListener {
                Toast.makeText(context, "${nodeName} post approved and moved to approved folder!", Toast.LENGTH_SHORT).show()
                onDismiss() // Dismiss the dialog
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to approve post.", Toast.LENGTH_SHORT).show()
        }
}

fun declinePost(post: BasePost, nodeName: String, context: Context, onDismiss: () -> Unit) {
    val database = FirebaseDatabase.getInstance(FIREBASE_URL)
    val pendingRef = database.getReference("pending${nodeName}s")
    val postId = post.title.replace(" ", "_").lowercase()

    pendingRef.child(postId).removeValue()
        .addOnSuccessListener {
            Toast.makeText(context, "$nodeName post declined and removed from pending folder!", Toast.LENGTH_SHORT).show()
            onDismiss() // Dismiss the dialog
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to remove post from pending folder.", Toast.LENGTH_SHORT).show()
        }
}
