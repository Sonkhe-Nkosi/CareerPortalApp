package com.example.mycareerportalapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Foreground Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo3),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Login", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Password input with visibility toggle
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        val visibilityIcon = if (passwordVisible) {
                            painterResource(id = R.drawable.visibility)
                        } else {
                            painterResource(id = R.drawable.visibility_off)
                        }
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(painter = visibilityIcon, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = Color.Blue)
                } else {
                    CustomButton(
                        text = "Login",
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Please enter both email and password"
                            } else {
                                isLoading = true
                                userLogin(navController, email, password) { error ->
                                    isLoading = false
                                    errorMessage = error
                                }
                            }
                        },
                        isLoading = isLoading
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { navController.navigate("ForgotPasswordScreen") }) {
                    Text("Forgot Password?", color = Color.Blue)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Back Arrow Button to navigate back to HomeScreen
                IconButton(onClick = { navController.popBackStack("HomeScreen", inclusive = false) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = Color.Blue
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                errorMessage?.let { message ->
                    Snackbar(
                        action = {
                            TextButton(onClick = { errorMessage = null }) {
                                Text("OK")
                            }
                        }
                    ) {
                        Text(text = message)
                    }
                }
            }
        }
    }
}

private fun userLogin(
    navController: NavController,
    email: String,
    password: String,
    setError: (String?) -> Unit
) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    // Store the user session (Optional: you can use SharedPreferences/ViewModel for this)
                    val database = FirebaseDatabase.getInstance()
                    val userRef = database.getReference("UserList")

                    userRef.get().addOnSuccessListener { snapshot ->
                        var foundRole: String? = null

                        // Check each role (Admin, Applicant, Employer, Undergraduate)
                        snapshot.children.forEach { roleSnapshot ->
                            roleSnapshot.children.forEach { userSnapshot ->
                                val userEmail = userSnapshot.child("email").getValue(String::class.java)
                                if (userEmail == email) {
                                    foundRole = roleSnapshot.key // The role is the key of the snapshot (Admin, Applicant, etc.)
                                    return@forEach // Exit the loop once the user is found
                                }
                            }
                        }

                        if (foundRole != null) {
                            // Optionally save role data for session management

                            // Navigate to the respective dashboard based on the role
                            when (foundRole) {
                                "Admin" -> navController.navigate("AdminDashboardScreen")
                                "Applicant" -> navController.navigate("ApplicantDashboardScreen")
                                "Employer" -> navController.navigate("EmployerDashboardScreen")
                                "Undergraduate" -> navController.navigate("UndergraduatesDashboardScreen")
                                else -> setError("Unknown role for user.")
                            }
                        } else {
                            setError("User not found or incorrect role.")
                        }

                    }.addOnFailureListener { exception ->
                        Log.e("UserLogin", "Failed to retrieve user data: ${exception.message}")
                        setError("Failed to retrieve user data.")
                    }
                }
            } else {
                Log.e("UserLogin", "Login failed: ${task.exception?.message}")
                setError(task.exception?.message ?: "Login failed")
            }
        }
}
