package com.example.mycareerportalapp


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicInformationScreen(navController: NavController) {
    Scaffold(
        topBar = { GenericTopBar(navController, "Academic Information") },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item { Text("Academic Information Content", style = MaterialTheme.typography.bodyLarge) }
            }
        }
    )
}