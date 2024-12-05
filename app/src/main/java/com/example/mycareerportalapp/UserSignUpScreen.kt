package com.example.mycareerportalapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSignUpScreen(navController: NavController) {
    var selectedRole by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var currentAddress by remember { mutableStateOf("") }
    var cellphoneNumber by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var studentNumber by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var signUpSuccess by remember { mutableStateOf(false) }
    val roleOptions = listOf("Undergraduate", "Employer", "Applicant", "Admin")
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo3),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Up")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedRole,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Select Role") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    roleOptions.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role) },
                            onClick = {
                                selectedRole = role
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input fields for sign-up
            TextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = currentAddress,
                onValueChange = { currentAddress = it },
                label = { Text("current Address") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = cellphoneNumber,
                onValueChange = { cellphoneNumber = it },
                label = { Text("cellphone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Conditional fields based on role
            if (selectedRole == "Employer") {
                TextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (selectedRole == "Applicant") {
                TextField(
                    value = studentNumber,
                    onValueChange = { studentNumber = it },
                    label = { Text("Student Number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color.Blue)
            } else {
                CustomButton(
                    text = if (isLoading) "SigningUp..." else "Sign Up",
                    onClick = {
                        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                            (selectedRole == "Employer" && companyName.isEmpty()) ||
                            (selectedRole == "Applicant" && studentNumber.isEmpty())

                        ) {

                            errorMessage = "Please fill in all the information"
                        } else if (password != confirmPassword) {
                            errorMessage = "Passwords do not match"
                        } else {
                            userSignUp(
                                navController, selectedRole, fullName, email, password,
                                currentAddress, cellphoneNumber, studentNumber, companyName, context
                            ) { error ->
                                if (error == null) {
                                    isLoading = true
                                    signUpSuccess = true // Sign-up successful, set flag
                                    Toast.makeText(
                                        context,
                                        "Sign up successful!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    errorMessage = error
                                }
                            }
                        }
                    },
                    modifier = Modifier.width(400.dp).height(50.dp),
                    isLoading = isLoading
                )
                // Back Arrow Button to navigate back to HomeScreen
                IconButton(onClick = {
                    navController.popBackStack(
                        "HomeScreen",
                        inclusive = false
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = Color.Blue
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("", color = Color.Blue)
                }


                // Show error dialog if necessary
                errorMessage?.let { message ->
                    AlertDialog(
                        onDismissRequest = { errorMessage = null },
                        confirmButton = {
                            TextButton(onClick = { errorMessage = null }) {
                                Text("OK")
                            }
                        },
                        text = { Text(text = message) }
                    )
                }
            }
        }
    }}
    private fun userSignUp(

        navController: NavController,
        role: String,
        fullName: String,
        email: String,
        password: String,
        currentAddress: String,
        cellphoneNumber: String,
        studentNumber: String?,
        companyName: String?,
        context: Context,
        setError: (String?) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: return@addOnCompleteListener
                    val userProfile = companyName?.let {
                        UserProfile(
                            userId = userId,
                            fullName = fullName,
                            email = email,
                            role = role,
                            phone = cellphoneNumber,
                            address = currentAddress,
                            profilePictureUrl = null.toString(),
                            studentNumber = studentNumber,
                            companyName = it
                        )
                    }

                    // Store user profile in Realtime Database under the role node
                    database.getReference("UserList").child(role).child(userId)
                        .setValue(userProfile)
                        .addOnSuccessListener {
                            setError(null) // Success, no error
                            Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate("UserLoginScreen") // Navigate after success
                        }
                        .addOnFailureListener { e ->
                            Log.e(
                                "FirebaseDatabaseError",
                                "Error saving user profile: ${e.message}"
                            )
                            setError("Failed to save user profile.")
                        }
                } else {
                    Log.e("FirebaseAuthError", "Error: ${task.exception?.message}")
                    setError(task.exception?.message ?: "Sign Up Failed")
                }
            }
    }
