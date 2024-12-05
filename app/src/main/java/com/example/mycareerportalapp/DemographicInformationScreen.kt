package com.example.mycareerportalapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemographicInformationScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Demographic info",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp, // Adjust the size to match the image
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle logo click if needed */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
                            contentDescription = "Logo",
                            tint = Color.Unspecified // Keep the original colors of the logo
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // White background
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp) // Adjust padding to align with the image
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Header Section with Gray Background
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Information Text
                                Text(
                                    text = "Here's the information you’ve provided about yourself. This will not be displayed on your profile.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                // "remove" Text
                                Row {
                                    Text(
                                        text = "You can always ",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "remove ",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline),
                                        modifier = Modifier.clickable {
                                            showDialog = true
                                        }
                                    )
                                    Text(
                                        text = "all personal demographic ",
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                }
                                Row{
                                    Text(
                                        text = "all personal demographic data submitted in these categories.",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Gender Section
                        SectionHeader(title = "Gender")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Select your gender identity",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DropdownMenuSample(
                            options = listOf("Select", "Male", "Female", "Other", "Prefer not to say")
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Disability Section
                        SectionHeader(title = "Disability")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Do you have a disability that substantially limits a major life activity, or a history of a disability?",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DropdownMenuSample(
                            options = listOf("Select", "Yes", "No", "Prefer not to say")
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        // Agree and Save Button
                        Button(
                            onClick = { /* Handle save */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = MaterialTheme.shapes.large,
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text(
                                text = "Agree and save",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    )

    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Remove Data")
            },
            text = {
                Text(text = "Are you sure you want to remove all personal demographic data?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        // Implement the logic to remove data here
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SectionHeader(title: String, fontSize: TextUnit = 16.sp) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize
        )
    )
}

@Composable
fun DropdownMenuSample(options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.first()) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { expanded = true }
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                    MaterialTheme.shapes.small
                )
                .padding(12.dp)
        ) {
            Text(
                text = selectedOption,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
    }
}
