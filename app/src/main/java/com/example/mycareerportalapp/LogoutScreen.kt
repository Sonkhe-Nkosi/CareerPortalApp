package com.example.mycareerportalapp


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutScreen(navController: NavController) {
    Scaffold(
        topBar = { GenericTopBar(navController, "Logout") },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Text("Are you sure you want to logout?", style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("LoginScreen") {
                            popUpTo("LogoutScreen") { inclusive = true }
                        }
                    }) {
                        Text("Logout")
                    }
                }
            }
        }
    )
}
