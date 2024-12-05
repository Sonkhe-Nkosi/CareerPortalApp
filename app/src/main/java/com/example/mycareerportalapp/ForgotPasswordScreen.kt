package com.example.mycareerportalapp

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CustomButton for sending reset email
        CustomButton(
            text = "Send Reset Email",
            onClick = {
                if (email.isNotBlank()) {
                    resetPassword(email) { result ->
                        message = result
                    }
                } else {
                    message = "Please enter a valid email address"
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        message?.let { text ->
            Snackbar(
                action = {
                    TextButton(onClick = { message = null }) {
                        Text("OK")
                    }
                }
            ) {
                Text(text = text)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.popBackStack() }
        ) {
            Text("Back to Login", color = Color.Blue)
        }
    }
}

private fun resetPassword(email: String, onComplete: (String) -> Unit) {
    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("ForgotPassword", "Password reset email sent to $email")
                onComplete("Password reset email sent successfully!")
            } else {
                Log.e("ForgotPassword", "Error sending password reset email", task.exception)
                onComplete(task.exception?.localizedMessage ?: "Failed to send reset email")
            }
        }
}
