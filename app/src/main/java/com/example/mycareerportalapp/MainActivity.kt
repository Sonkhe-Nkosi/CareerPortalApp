package com.example.mycareerportalapp

import NavigationSystem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mycareerportalapp.ui.theme.MyCareerPortalAppTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val userId get() = auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        generateAndStoreFirebaseToken()

        setContent {
            MyCareerPortalAppTheme {
                val navController = rememberNavController() as NavHostController
                NavigationSystem(start = "LoadingScreen", navController = navController)

            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUserStatus("Online")
    }

    override fun onPause() {
        super.onPause()
        updateUserStatus("Offline")
    }

    private fun generateAndStoreFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                userId?.let { uid ->
                    firestore.collection("Tokens").document(uid).set(mapOf("token" to token))
                }
            } else {
                task.exception?.printStackTrace()
            }
        }
    }

    private fun updateUserStatus(status: String) {
        userId?.let { uid ->
            firestore.collection("Users").document(uid).update("status", status)
        }
    }


}
