package com.example.mycareerportalapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


data class Applicant(
    val applicantId: String = "",
    val posterName: String = "",
    val postReference: String = "",
    val summary: String = "",
    val fullName: String = "",
    val email: String = "",
    val resumeUrl: String = "",
    val applicationStatus: String = "Pending"
)

enum class PostType(val nodeName: String) {
    BURSARY("BursaryPost"),
    LEARNERSHIP("LearnershipPost"),
    JOB("JobPost"),
    INTERNSHIP("InternshipPost"),
    UPDATE("UpdatePost")
}
open class EmployerBasePost(
    open val postId: String = "",
    open val employerId: String = "",
    open val postReference: String = "",
    open val title: String = "",
    open val description: String = "",
    open val imageUri: String = "",
    open val selectedPostId: Boolean = false,
    open val approved: Boolean = false
) {
    // Renamed method to avoid conflict
    open fun toCopy(

        postId: String = this.postId,
        employerId: String = this.employerId,
        title: String = this.title,
        description: String = this.description,
        imageUri: String = this.imageUri,
        approved: Boolean = this.approved,
        selectedPostId: Boolean= this.approved,
    ): EmployerBasePost {
        return EmployerBasePost(postId, title, description, imageUri, approved.toString())
    }

    open val nodeEmployerName: String
        get() {
            return when (this) {
                is EmployerBursaryPost -> "bursaryPosts"
                is EmployerLearnershipPost -> "learnershipPosts"
                is EmployerJobPost -> "jobPosts"
                is EmployerInternshipPost -> "internshipPosts"
                is EmployerUpdatePost -> "updatePosts"
                else -> "genericPosts"
            }
        }
}



data class EmployerBursaryPost(
    override val postId: String = "",
    override val employerId: String = "",
    override val title: String = "",
    override val description: String = "",
    override val imageUri: String = "",
    override val approved: Boolean = false,
    override val selectedPostId: Boolean = false,
    val bursaryAmount: String = "",
    val applicationDeadline: String = "",
    override val postReference: String = ""
) : EmployerBasePost(postId, title, description, imageUri, approved.toString()) {

    constructor() : this("", "", "", "", "", false, false, "", "", "")

    override fun toCopy(
        postId: String,
        employerId: String,
        title: String,
        description: String,
        imageUri: String,
        approved: Boolean,
        selectedPostId: Boolean
    ): EmployerBursaryPost {
        return EmployerBursaryPost(
            postId,
            title,
            description,
            imageUri,
            approved.toString(),
            postReference = postReference
        )
    }
}

// Repeat similarly for LearnershipPost, JobPost, InternshipPost, UpdatePost...

data class EmployerLearnershipPost(
    override val postId: String = "",
    override val employerId: String = "",
    val learnershipTitle: String = "",
    val learnershipDescription: String = "",
    override val imageUri: String = "",
    override val approved: Boolean = false,
    override val selectedPostId: Boolean = false,
    val learnershipDuration: String = "", // Additional field
    val applicationDeadline: String = "", // Additional field
    override val postReference: String = ""
) : EmployerBasePost(postId, learnershipTitle, learnershipDescription, imageUri, approved.toString())

data class EmployerUpdatePost(
    override val postReference: String = "",
    override val postId: String = "",
    val updateTitle: String = "",
    val updateDescription: String = "",
    override val approved: Boolean = false,
    override val selectedPostId: Boolean = false,
) : EmployerBasePost(postId, updateTitle, updateDescription)

data class EmployerJobPost(
    override val postId: String = "",
    override val employerId: String = "",
    val jobTitle: String = "",
    val jobDescription: String = "",
    override val imageUri: String = "",
    override val approved: Boolean = false,
    override val selectedPostId: Boolean = false,
    val jobLocation: String = "", // Additional field
    val salaryRange: String = "", // Additional field
    val applicationDeadline: String = "",
    override val postReference: String = ""
) : EmployerBasePost(postId,employerId, jobTitle, jobDescription, imageUri, approved.toString())

data class EmployerInternshipPost(
    override val postId: String = "",
    override val employerId: String = "",
    val internshipTitle: String = "",
    val internshipDescription: String = "",
    override val imageUri: String = "",
    override val approved: Boolean = false,
    override val selectedPostId: Boolean = false,
    val internshipDuration: String = "", // Additional field
    val applicationDeadline: String = "",
    override val postReference: String = ""
) : EmployerBasePost(postId, internshipTitle, internshipDescription, imageUri, approved.toString())

data class ChatItem(
    val id: String = "",
    val name: String = "",
    val lastMessage: String = "",
    val profileImage: String = "",
    val timestamp: Long = 0L // Timestamp to track when the last message was sent
)

@Composable
fun CustomButton(
    text: String,
    height: Dp = 50.dp,
    width: Dp = 500.dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier
            .width(width)
            .height(height),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF021C5B),
            contentColor = Color.White
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(text = text)
        }
    }
}


@Composable
fun ApplicantsBottomNavigationBar(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        BottomAppBar(
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("ApplicantDashboardScreen")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.img_3), contentDescription = "Menu")
                        }
                        Text(text = "Home", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("TrackMyStatusScreen")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.track), contentDescription = "Track My StatusScreen")
                        }
                        Text(text = "Track Status", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Dropdown Button
                        Box {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(painter = painterResource(id = R.drawable.addbuttonicon), contentDescription = "Add Post")
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Bursary") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("BursaryPostingScreen")
                                    }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Learnership") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("LearnershipPostingScreen")
                                    }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Job") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("JobPostingScreen")
                                    }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Internship") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("InternshipPostingScreen")
                                    }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Updates") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("UpdatePostingScreen")
                                    }
                                )
                            }
                        }
                        Text(text = "Add", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("ResumeFormScreen")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.resume), contentDescription = "Create Resume")
                        }
                        Text(text = "Create Resume", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("ChatScreen/{contactName}/{contactProfileImage}")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.img_6), contentDescription = "Chat")
                        }
                        Text(text = "Chat", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantDashboardTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(50.dp)
                )
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.dropdown),
                        contentDescription = "Menu",
                        modifier = Modifier.size(50.dp)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.Transparent)
                    ) {
                        androidx.compose.material3.DropdownMenuItem(
                            onClick = { navController.navigate("ApplicantDashboardScreen") },
                            text = { Text("Homepage") }
                        )
                        androidx.compose.material3.DropdownMenuItem(
                            onClick = { navController.navigate("TrackMyStatusScreen") },
                            text = { Text("Track My StatusScreen") }
                        )
                        androidx.compose.material3.DropdownMenuItem(
                            onClick = { navController.navigate("ResumeFormScreen") },
                            text = { Text("Create a Resume") }
                        )

                        androidx.compose.material3.DropdownMenuItem(
                            onClick = {
                                FirebaseAuth.getInstance().signOut()
                                navController.navigate("UserLoginScreen") {
                                    popUpTo("ApplicantDashboardScreen") { inclusive = true }
                                }
                            },
                            text = { Text("Logout") }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(240, 240, 241, 255),
            titleContentColor = Color(2, 24, 100, 255)
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        BottomAppBar(
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("UndergraduatesDashboardScreen")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.img_3), contentDescription = "Menu")
                        }
                        Text(text = "Home", style = MaterialTheme.typography.labelMedium)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Dropdown Button
                        Box {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(painter = painterResource(id = R.drawable.addbuttonicon), contentDescription = "Add Post")
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Bursary") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("BursaryPostingScreen")
                                    }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Learnership") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("LearnershipPostingScreen")
                                    }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Job") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("JobPostingScreen")
                                    }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Internship") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("InternshipPostingScreen")
                                    }
                                )
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text("Updates") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate("UpdatePostingScreen")
                                    }
                                )
                            }
                        }
                        Text(text = "Add", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("DiscoverCoursesScreen")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.img_5), contentDescription = "Discover Course")
                        }
                        Text(text = "Discover Course", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("ChatScreen/{contactName}/{contactProfileImage}")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.img_6), contentDescription = "Chat")
                        }
                        Text(text = "Chat", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UndergraduatesDashboardScreenTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(50.dp)
                )
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.dropdown),
                        contentDescription = "Menu",
                        modifier = Modifier.size(50.dp)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.Transparent)
                    ) {
                        androidx.compose.material3.DropdownMenuItem(
                            onClick = { navController.navigate("UndergraduatesDashboardScreen") },
                            text = { Text("Homepage") }
                        )
                        androidx.compose.material3.DropdownMenuItem(
                            onClick = { navController.navigate("CareerGuidanceScreen") },
                            text = { Text("Career Guidance") }
                        )
                        androidx.compose.material3.DropdownMenuItem(
                            onClick = { navController.navigate("DiscoverCoursesScreen") },
                            text = { Text("Find Course") }
                        )

                        androidx.compose.material3.DropdownMenuItem(
                            onClick = {
                                FirebaseAuth.getInstance().signOut()
                                navController.navigate("UserLoginScreen") {
                                    popUpTo("UndergraduatesDashboardScreen") { inclusive = true }
                                }
                            },
                            text = { Text("Logout") }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(240, 240, 241, 255),
            titleContentColor = Color(2, 24, 100, 255)
        )
    )
}

@Composable
fun EmployerBottomNavigationBar(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        BottomAppBar(
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("EmployerDashboardScreen")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.img_3), contentDescription = "Menu")
                        }
                        Text(text = "Home", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("PosterHistoryScreen")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.poster_history), contentDescription = "history")
                        }
                        Text(text = "History", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton( onClick = {
                                navController.navigate("CreatePosterScreen/{posterId}/{applicantId}")
                            }) {
                                Icon(painter = painterResource(id = R.drawable.addbuttonicon), contentDescription = "Add Post")
                            }
                            Text(text = "add post", style = MaterialTheme.typography.labelMedium)
                        }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("ApplicantListScreen/{posterId}/{applicantId}")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.applicants_icon), contentDescription = "Show list of applications")
                        }
                        Text(text = "Applications", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate("ChatScreen/{contactName}/{contactProfileImage}")
                        }) {
                            Icon(painter = painterResource(id = R.drawable.img_6), contentDescription = "Chat")
                        }
                        Text(text = "Chat", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerDashboardTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(50.dp)
                )
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.dropdown),
                        contentDescription = "Menu",
                        modifier = Modifier.size(50.dp)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.Transparent)
                    ) {
                        androidx.compose.material3.DropdownMenuItem(
                            onClick = { navController.navigate("EmployerDashboardScreen") },
                            text = { Text("Homepage") }
                        )
                        androidx.compose.material3.DropdownMenuItem(
                            onClick = { navController.navigate("PosterHistoryScreen") },
                            text = { Text("Recent Posts") }
                        )
                        androidx.compose.material3.DropdownMenuItem(
                            onClick = { navController.navigate("ApplicantListScreen/{posterId}/{applicantId}") },
                            text = { Text("Check who applied") }
                        )

                        androidx.compose.material3.DropdownMenuItem(
                            onClick = {
                                FirebaseAuth.getInstance().signOut()
                                navController.navigate("UserLoginScreen") {
                                    popUpTo("EmployerDashboardScreen") { inclusive = true }
                                }
                            },
                            text = { Text("Logout") }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(240, 240, 241, 255),
            titleContentColor = Color(2, 24, 100, 255)
        )
    )
}
