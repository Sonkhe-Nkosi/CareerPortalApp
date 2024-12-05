package com.example.mycareerportalapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerGuidanceScreen(navController: NavController) {
    var currentScreen by remember { mutableStateOf("Main") }
    var showImageDialog by remember { mutableStateOf(false) }
    var imageResourceId by remember { mutableStateOf(R.drawable.img) }
    var imageDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = { CareerGuidanceGenericTopBar(navController, "Career Guidance") },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (currentScreen == "Courses") {
                    Button(
                        onClick = { currentScreen = "Main" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Back", color = Color.White)
                    }
                }

                when (currentScreen) {
                    "Main" -> {
                        MainContent(
                            onCoursesClick = { currentScreen = "Courses" },
                            onCareerOpportunitiesClick = { currentScreen = "Career Opportunities" },
                            onImageClick = { imageId, description ->
                                imageResourceId = imageId
                                imageDescription = description
                                showImageDialog = true
                            },
                            paddingValues = paddingValues
                        )
                    }
                    "Courses" -> {
                        CoursesContent(onBack = { currentScreen = "Main" })
                    }
                    "Career Opportunities" -> {
                        CareerOpportunitiesContent(onBack = { currentScreen = "Main" })
                    }
                }

                if (showImageDialog) {
                    ImageDialog(
                        imageResourceId = imageResourceId,
                        imageDescription = imageDescription,
                        onDismiss = { showImageDialog = false }
                    )
                }
            }
        }
    )
}

@Composable
fun MainContent(
    onCoursesClick: () -> Unit,
    onCareerOpportunitiesClick: () -> Unit,
    onImageClick: (Int, String) -> Unit,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.img_2),
                contentDescription = "Banner Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Column {
                Button(
                    onClick = onCoursesClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Courses")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCareerOpportunitiesClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Career Opportunities")
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Display images with their names
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ict),
                            contentDescription = "Diploma in ICT",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                .clickable { onImageClick(R.drawable.ict, "Diploma in ICT") }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "ICT", style = MaterialTheme.typography.bodySmall)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.law),
                            contentDescription = "Diploma in Law",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                .clickable { onImageClick(R.drawable.law, "Diploma in Law") }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Law", style = MaterialTheme.typography.bodySmall)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hosp),
                            contentDescription = "Diploma in Hospitality",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                .clickable { onImageClick(R.drawable.hosp, "Diploma in Hospitality") }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Hospitality", style = MaterialTheme.typography.bodySmall)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.agric),
                            contentDescription = "Diploma in Agriculture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                .clickable { onImageClick(R.drawable.agric, "Diploma in Agriculture") }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Agriculture", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerGuidanceGenericTopBar(navController: NavController, title: String) {
    SmallTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = title,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        actions = {},
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White),
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    )
}

@Composable
fun CoursesContent(onBack: () -> Unit) {
    var selectedCourse by remember { mutableStateOf<Course?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Content of the Courses screen
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Courses",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            val courses = listOf(
                Course("Diploma in Hospitality Management", "Three years", """
                    Theoretical and practical aspects of Hospitality Management, including:
                    - Accommodation Management
                    - Culinary Studies and Nutrition
                    - Food and Beverage Studies and Operations
                    - Financial Management
                    - Hospitality Management
                    - Hospitality Service Excellence
                    - Hospitality Health and Safety
                    - Hospitality Law
                    - Support modules: Hospitality Communication, Computing, First Aid
                    Work-integrated learning during the second and third years.
                """),
                Course("Diploma in ICT", "Three years", """
                    Core subjects include:
                    - Information Systems
                    - Programming and Software Development
                    - Networking
                    - Databases
                    - Web Development
                    - IT Project Management
                    Practical components with internships.
                """),
                Course("Diploma in Law", "Three years", """
                    Core subjects include:
                    - Legal Theory
                    - Constitutional Law
                    - Criminal Law
                    - Civil Procedure
                    - Property Law
                    - Family Law
                    - Contract Law
                    Practical components with legal clinics.
                """),
                Course("Diploma in Agriculture", "Three years", """
                    Core subjects include:
                    - Crop Production
                    - Soil Science
                    - Animal Husbandry
                    - Agricultural Economics
                    - Farm Management
                    - Irrigation and Water Management
                    - Sustainable Agriculture
                    Practical components with farm work experience.
                """)
            )

            LazyColumn {
                items(courses) { course ->
                    CourseCard(
                        title = course.title,
                        duration = course.duration,
                        description = course.description,
                        onClick = { selectedCourse = course }
                    )
                }
            }

            selectedCourse?.let { course ->
                CourseDetailDialog(course = course, onDismiss = { selectedCourse = null })
            }
        }

        // Back Button at the bottom
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Back", color = Color.White)
        }
    }
}



@Composable
fun CourseCard(title: String, duration: String, description: String, onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable(onClick = onClick) // Make card clickable
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = duration,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailDialog(course: Course, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Course Details") },
        text = {
            Column {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = course.duration,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },

        )
}


@Composable
fun CareerOpportunitiesContent(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Back", color = Color.White)
        }

        Text(
            text = "Career Opportunities",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        val opportunities = listOf(
            CareerOpportunity("Software Engineer", "5 opportunities available"),
            CareerOpportunity("Data Scientist", "3 opportunities available"),
            CareerOpportunity("Product Manager", "2 opportunities available"),
            CareerOpportunity("UX Designer", "4 opportunities available")
        )

        LazyColumn {
            items(opportunities) { opportunity ->
                CareerOpportunityItem(
                    title = opportunity.title,
                    opportunities = opportunity.opportunities,
                    onImageClick = { imageId, description ->
                        // Handle image click if needed
                    }
                )
            }
        }
    }
}

@Composable
fun CareerOpportunityItem(title: String, opportunities: String, onImageClick: (Int, String) -> Unit) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = opportunities,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ImageDialog(imageResourceId: Int, imageDescription: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Image Preview")
        },
        text = {
            Image(
                painter = painterResource(id = imageResourceId),
                contentDescription = imageDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

data class Course(val title: String, val duration: String, val description: String)
data class CareerOpportunity(val title: String, val opportunities: String)