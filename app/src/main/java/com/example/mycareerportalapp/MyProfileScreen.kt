@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mycareerportalapp

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun MyProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser


    if (currentUser == null) {

        LaunchedEffect(Unit) {
            navController.navigate("UserLoginScreen") {
                popUpTo("MyProfileScreen") { inclusive = true }
            }
        }
        return
    }

    val userId = currentUser.uid
    val database = FirebaseDatabase.getInstance("https://my-career-portal-app-default-rtdb.firebaseio.com/")
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch user profile from the specified database path
    LaunchedEffect(userId) {
        val profileRef = database.getReference("EvaluatedApplications/UserList/$userId")
        profileRef.get().addOnSuccessListener { snapshot ->
            userProfile = snapshot.getValue(UserProfile::class.java)
            isLoading = false
        }.addOnFailureListener {
            isLoading = false
            Log.e("FirebaseError", "Error fetching user profile.")
        }
    }

    // Show loading indicator while fetching data
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Blue)
        }
        return
    }

    // Display profile if available
    userProfile?.let {
        ProfileDisplay(
            profilePictureUri = it.profilePictureUrl,
            headline = it.fullName,
            aboutText = it.aboutText,
            experienceList = it.workExperience,
            educationList = it.education,
            skillsList = it.skills,
            projectsList = it.honorsAwards,
            recommendationsText = it.objective
        )
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "User profile not found")
        }
    }
}


@Composable
fun ProfileDisplay(
    profilePictureUri: String?,
    headline: String,
    aboutText: String,
    experienceList: List<WorkExperience>,
    educationList: List<Education>,
    skillsList: List<String>,
    projectsList: List<String>,
    recommendationsText: String
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        ) {
            if (profilePictureUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(profilePictureUri),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Default Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Headline
        Text(text = headline, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(4.dp))

        // About Text
        Text(text = aboutText, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Experience
        Text(text = "Experience", style = MaterialTheme.typography.titleMedium)
        experienceList.forEach { experience ->
            Text(text = "${experience.title} at ${experience.companyName} from ${experience.startDate} to ${experience.endDate}", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Education
        Text(text = "Education", style = MaterialTheme.typography.titleMedium)
        educationList.forEach { education ->
            Text(text = "${education.qualification} from ${education.institutionName} (Start: ${education.startYear}, Graduated: ${education.graduationYear})", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Skills
        Text(text = "Skills", style = MaterialTheme.typography.titleMedium)
        skillsList.forEach { skill ->
            Text(text = skill, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Projects
        Text(text = "Projects", style = MaterialTheme.typography.titleMedium)
        projectsList.forEach { project ->
            Text(text = project, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Recommendations
        Text(text = "Recommendations", style = MaterialTheme.typography.titleMedium)
        Text(text = recommendationsText, style = MaterialTheme.typography.bodyMedium)
    }
}
