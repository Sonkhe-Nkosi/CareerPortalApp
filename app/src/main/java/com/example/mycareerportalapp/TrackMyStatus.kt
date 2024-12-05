package com.example.mycareerportalapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackMyStatusScreen(navController: NavController) {
    var applications by remember { mutableStateOf(listOf<Applicant>()) }
    val databaseRefPending = FirebaseDatabase.getInstance().getReference("pendingApplications")
    val databaseRefEvaluated = FirebaseDatabase.getInstance().getReference("EvaluatedApplications")

    DisposableEffect(Unit) {
        val pendingListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pendingApps = snapshot.children.mapNotNull { it.getValue(Applicant::class.java) }
                applications = applications + pendingApps
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle Firebase error for pending applications
            }
        }

        val evaluatedListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val evaluatedApps = snapshot.children.mapNotNull { it.getValue(Applicant::class.java) }
                applications = applications + evaluatedApps
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle Firebase error for evaluated applications
            }
        }

        databaseRefPending.addValueEventListener(pendingListener)
        databaseRefEvaluated.addValueEventListener(evaluatedListener)

        // Cleanup logic
        onDispose {
            databaseRefPending.removeEventListener(pendingListener)
            databaseRefEvaluated.removeEventListener(evaluatedListener)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track My Status") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(applications) { application ->
                TrackMyStatusItem(application) {
                    // Navigate to a details screen or perform another action
                    navController.navigate("DetailsScreen/${application.postReference}")
                }
            }
        }
    }
}

@Composable
fun TrackMyStatusItem(application: Applicant, onViewDetailsClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Poster: ${application.posterName}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Post Reference: ${application.postReference}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Status: ${application.applicationStatus}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
