package com.example.mycareerportalapp

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState")
@Composable
fun ResumeFormScreen(navController: androidx.navigation.NavController) {

    var fullName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var languages by remember { mutableStateOf("") }
    var honorsAwards by remember { mutableStateOf("") }
    var certifications by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }
    val emailError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var educationList by remember { mutableStateOf(mutableListOf<Education>()) }
    var studentNumber by remember { mutableStateOf("") }
    var workExperienceList by remember { mutableStateOf(mutableListOf<WorkExperience>()) }
    var showAddEducationDialog by remember { mutableStateOf(false) }
    var showAddWorkExperienceDialog by remember { mutableStateOf(false) }
    var profilePictureUri by remember { mutableStateOf<Uri?>(null) }
    var showResumePreviewDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            profilePictureUri = uri // Set the picked image URI
        }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Curriculum Vitae")
        Spacer(modifier = Modifier.height(16.dp))
        Divider()

        CustomButton(
            text = "Pick Profile Picture",
            onClick = { pickImageLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        )

        if (profilePictureUri != null) {
            Image(
                painter = rememberImagePainter(profilePictureUri),
                contentDescription = "Selected Profile Picture",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
        }

        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        TextField(
            value = role,
            onValueChange = { role = it },
            label = { Text("Role (e.g., Graphic Designer)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = objective,
            onValueChange = { objective = it },
            label = { Text("Objective") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Birth Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Education")
        Spacer(modifier = Modifier.height(16.dp))
        Divider()

        if (showAddEducationDialog) {
            AddEducationDialog(
                onDismiss = { showAddEducationDialog = false },
                onSave = { education ->
                    educationList.add(education)
                    showAddEducationDialog = false
                }
            )
        }
        CustomButton(
            text = "Add Education",
            onClick = { showAddEducationDialog = true },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Work Experience")
        Spacer(modifier = Modifier.height(16.dp))
        Divider()

        if (showAddWorkExperienceDialog) {
            AddWorkExperienceDialog(
                onDismiss = { showAddWorkExperienceDialog = false },
                onSave = { workExperience ->
                    workExperienceList.add(workExperience)
                    showAddWorkExperienceDialog = false
                }
            )
        }

        CustomButton(
            text = "Add Work Experience",
            onClick = { showAddWorkExperienceDialog = true },
            modifier = Modifier.fillMaxWidth()
        )

        CustomButton(
            text = "Preview Resume",
            onClick = { showResumePreviewDialog = true },
            modifier = Modifier.fillMaxWidth()
        )

        if (showResumePreviewDialog) {
            ShowResumePreviewDialog(
                fullName = fullName,
                role = role,
                objective = objective,
                gender = gender,
                birthDate = birthDate,
                phone = phone,
                email = email,
                address = address,
                skills = parseCommaSeparatedInput(skills),
                languages = parseCommaSeparatedInput(languages),
                honorsAwards = parseCommaSeparatedInput(honorsAwards),
                certifications = parseCommaSeparatedInput(certifications),
                interests = parseCommaSeparatedInput(interests),
                educationList = educationList,
                workExperienceList = workExperienceList,
                profilePictureUri = profilePictureUri,
                onDismiss = { showResumePreviewDialog = false }
            )
        }

        Divider()

        // Add a new state to track the loading status
        var isLoading by remember { mutableStateOf(false) }

        CustomButton(
            text = if (isLoading) "Creating..." else "Create Resume",
            onClick = {
                if (fullName.isNotEmpty() && role.isNotEmpty() && !emailError) {
                    val userId = Firebase.auth.currentUser?.uid.orEmpty()
                    isLoading = true

                    if (profilePictureUri != null) {
                        uploadProfilePicture(profilePictureUri!!) { profilePicUrl ->
                            val resume = UserProfile(
                                userId = userId,
                                fullName = fullName,
                                role = role,
                                objective = objective,
                                gender = gender,
                                birthDate = birthDate,
                                phone = phone,
                                email = email,
                                address = address,
                                profilePictureUrl = profilePicUrl,
                                education = educationList,
                                studentNumber = studentNumber,
                                workExperience = workExperienceList,
                                skills = parseCommaSeparatedInput(skills),
                                languages = parseCommaSeparatedInput(languages),
                                honorsAwards = parseCommaSeparatedInput(honorsAwards),
                                certifications = parseCommaSeparatedInput(certifications),
                                interests = parseCommaSeparatedInput(interests)
                            )
                            saveResumeToDatabase(resume) { success ->
                                if (success) {
                                    navController.navigate("ApplicantDashboardScreen") // Navigate on success
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Failed to save resume.")
                                    }
                                }
                                isLoading = false
                            }
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please upload a profile picture.")
                        }
                        isLoading = false
                    }
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Please fill out all required fields.")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading // Disable button while loading
        )

    }
}

        @Composable
fun AddEducationDialog(
    onDismiss: () -> Unit,
    onSave: (Education) -> Unit
) {
    var schoolName by remember { mutableStateOf("") }
    var qualification by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Education") },
        text = {
            Column {
                TextField(value = schoolName, onValueChange = { schoolName = it }, label = { Text("School Name") })
                TextField(value = qualification, onValueChange = { qualification = it }, label = { Text("Qualification") })
                TextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Start Date") })
                TextField(value = endDate, onValueChange = { endDate = it }, label = { Text("End Date") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(Education(schoolName, qualification, startDate, endDate))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun saveResumeToDatabase(resume: UserProfile, onComplete: (Boolean) -> Unit) {
    val database = Firebase.database("https://my-career-portal-app-default-rtdb.firebaseio.com/")
    val resumesRef = database.getReference("resume")
    val newResumeRef = resumesRef.push()

    // Save the main resume data
    newResumeRef.setValue(resume)
        .addOnSuccessListener {
            // Save education details
            val educationRef = newResumeRef.child("education")
            resume.education.forEach { education ->
                educationRef.push().setValue(education)
            }

            // Save work experience details
            val workExperienceRef = newResumeRef.child("workExperience")
            resume.workExperience.forEach { workExperience ->
                workExperienceRef.push().setValue(workExperience)
            }

            Log.i("ResumeForm", "Resume successfully saved!")
            onComplete(true)
        }
        .addOnFailureListener { exception ->
            Log.e("ResumeForm", "Error saving resume: ${exception.message}", exception)
            onComplete(false)
        }
}


fun parseCommaSeparatedInput(input: String): List<String> {
    return input.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
@Composable
fun AddWorkExperienceDialog(
    onDismiss: () -> Unit,
    onSave: (WorkExperience) -> Unit
) {
    var companyName by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Work Experience") },
        text = {
            Column {
                TextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = jobTitle,
                    onValueChange = { jobTitle = it },
                    label = { Text("Job Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Start Date") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("End Date") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = jobDescription,
                    onValueChange = { jobDescription = it },
                    label = { Text("Job Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    WorkExperience(
                        companyName = companyName,
                        jobTitle = jobTitle,
                        startDate = startDate,
                        endDate = endDate,
                        description = jobDescription
                    )
                )
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
fun uploadProfilePicture(uri: Uri, onComplete: (String) -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val profilePicsRef = storageRef.child("resume")

    val uploadTask = profilePicsRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        profilePicsRef.downloadUrl.addOnSuccessListener { downloadUrl ->
            val profilePicUrl = downloadUrl.toString()
            onComplete(profilePicUrl)
        }
    }.addOnFailureListener {
        Log.e("ResumeForm", "Profile picture upload failed", it)
        onComplete("failed to upload")
    }
}

@Composable
fun ShowResumePreviewDialog(
    fullName: String,
    role: String,
    objective: String,
    gender: String,
    birthDate: String,
    phone: String,
    email: String,
    address: String,
    skills: List<String>,
    languages: List<String>,
    honorsAwards: List<String>,
    certifications: List<String>,
    interests: List<String>,
    educationList: List<Education>,
    workExperienceList: List<WorkExperience>,
    profilePictureUri: Uri?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Resume Preview") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                // Profile Picture
                if (profilePictureUri != null) {
                    Text("Profile Picture:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberImagePainter(profilePictureUri),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Personal Information
                Text("Full Name: $fullName")
                Text("Role: $role")
                Text("Objective: $objective")
                Text("Gender: $gender")
                Text("Birth Date: $birthDate")
                Text("Phone: $phone")
                Text("Email: $email")
                Text("Address: $address")
                Spacer(modifier = Modifier.height(16.dp))

                // Skills
                if (skills.isNotEmpty()) {
                    Text("Skills:")
                    skills.forEach { Text("- $it") }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Languages
                if (languages.isNotEmpty()) {
                    Text("Languages:")
                    languages.forEach { Text("- $it") }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Honors & Awards
                if (honorsAwards.isNotEmpty()) {
                    Text("Honors & Awards:")
                    honorsAwards.forEach { Text("- $it") }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Certifications
                if (certifications.isNotEmpty()) {
                    Text("Certifications:")
                    certifications.forEach { Text("- $it") }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Interests
                if (interests.isNotEmpty()) {
                    Text("Interests:")
                    interests.forEach { Text("- $it") }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Education
                if (educationList.isNotEmpty()) {
                    Text("Education:")
                    educationList.forEach { education ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("School: ${education.institutionName}")
                        Text("Qualification: ${education.qualification}")
                        Text("Start Date: ${education.startYear}")
                        Text("End Date: ${education.graduationYear}")
                        Divider()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Work Experience
                if (workExperienceList.isNotEmpty()) {
                    Text("Work Experience:")
                    workExperienceList.forEach { experience ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Company: ${experience.companyName}")
                        Text("Job Title: ${experience.jobTitle}")
                        Text("Start Date: ${experience.startDate}")
                        Text("End Date: ${experience.endDate}")
                        Text("Description: ${experience.description}")
                        Divider()
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }

    )
}
