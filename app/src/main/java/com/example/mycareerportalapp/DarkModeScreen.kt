package com.example.mycareerportalapp

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import androidx.compose.material3.Typography


import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewModelScope

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),
    */
)


// Enum to define theme modes
enum class ThemeMode { DEVICE, DARK, LIGHT }

// ViewModel to manage the theme state
class ThemeViewModel : ViewModel() {
    var themeMode by mutableStateOf(ThemeMode.DEVICE)
        private set

    fun updateThemeMode(mode: ThemeMode) {
        themeMode = mode
    }
}




// Function to apply the theme across the app
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@Composable
fun MyCareerPortalApp(themeViewModel: ViewModel = viewModel()) {
    val themeMode = remember { themeViewModel.viewModelScope }  // Use remember to track the theme mode

    // Determine the theme based on the selected mode
    /* val isDarkTheme = when (themeMode) {
         ThemeMode.DEVICE -> isSystemInDarkTheme()
         ThemeMode.DARK -> true
         ThemeMode.LIGHT -> false
         else -> {}
     }*/

    /*  MyTheme(darkTheme = isDarkTheme) {
          // App's navigation and main content
          val navController = rememberNavController()
          NavigationSystem(navController.toString())*/
    /* Scaffold(
         topBar = { /* Define your top bar here */ },
         bottomBar = { BottomNavigationBar(navController) },
         content = {
             NavHost(navController, startDestination = start) {
                 composable("ApplicantLoginScreen") {
                     ApplicantLoginScreen(navController =navController)
                 }
                 composable("ApplicantSignUpScreen") {
                     ApplicantSignUpScreen(navController = navController)
                 }
                 composable("HomeScreen") {
                     HomeScreen(navController = navController)
                 }
                 composable("LoadingScreen") {
                     LoadingScreen(navController = navController)
                 }
                 composable("ApplicantDashboardScreen") {
                     ApplicantDashboardScreen(navController = navController)
                 }
                 composable("AdminLoginScreen") {
                     AdminLoginScreen(navController = navController)
                 }
                 composable("AdminSignUpScreen") {
                     AdminSignUpScreen(navController = navController)
                 }
                 composable("StudentGraduatesLoginScreen") {
                     StudentGraduatesLoginScreen(navController = navController)
                 }
                 composable("StudentGraduatesSignUpScreen") {
                     StudentGraduatesSignUpScreen(navController = navController)
                 }
                 composable("AlumniEmployerLoginScreen") {
                     AlumniEmployerLoginScreen(navController = navController)
                 }
                 composable("AlumniEmployerSignUpScreen") {
                     AlumniEmployerSignUpScreen(navController = navController)
                 }
                 composable("PostingScreen") {
                     PostingScreen(navController = navController)
                 }
                 composable("ApsCalculatorScreen") {
                     ApsCalculatorScreen(navController = navController)
                 }
                 composable("AdminDashboardScreen") {
                     AdminDashboardScreen(navController = navController)
                 }
                 composable("PostingScreenAdmin") {
                     PostingScreenAdmin(navController = navController)
                 }
                 composable("ChatScreen") {
                     ChatScreen(navController = navController)
                 }
                 composable("DiscoverCoursesScreen") {
                     DiscoverCoursesScreen(navController = navController)
                 }
                 composable("SettingsScreen") {
                     SettingsScreen(navController = navController)
                 }
                 composable("SkillsAndInterestsScreen") {
                     SkillsAndInterestsScreen(navController = navController)
                 }
                 composable("AcademicInformationScreen") {
                     AcademicInformationScreen(navController = navController)
                 }
                 composable("NotificationScreen") {
                     NotificationScreen(navController = navController)
                 }
                 composable("UploadResumeScreen") {
                     UploadResumeScreen(navController = navController)
                 }
                 composable("SearchFAQsScreen") {
                     SearchFAQsScreen(navController = navController)
                 }
                 composable("AboutAppScreen") {
                     AboutAppScreen(navController = navController)
                 }
                 composable("GeneralSettingsScreen") {
                     GeneralSettingsScreen(navController = navController)
                 }
                 composable("SecurityAndPrivacyScreen") {
                     SecurityAndPrivacyScreen(navController = navController)
                 }
                 composable("LogoutScreen") {
                     LogoutScreen(navController = navController)
                 }
                 composable("ForgotPasswordScreen") {
                     ForgotPasswordScreen(navController = navController)
                 }
                 composable("CareerGuidanceScreen") {
                     CareerGuidanceScreen(navController = navController)
                 }
                 composable("MyProfileScreen") {
                     MyProfileScreen(navController = navController)
                 }
                 composable("DemographicInformationScreen") {
                     DemographicInformationScreen(navController = navController)
                 }
                 composable("DarkModeScreen") {
                     DarkModeScreen(navController = navController)
                 }*/
    // Add more screens as needed
}
//}
// )
// }
//}


// Custom Theme function for light and dark mode
@Composable
fun MyTheme(darkTheme: Any = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = (if (darkTheme as Boolean) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }).apply {

        MaterialTheme(
            colorScheme = this,
            typography = Typography,
            content = content
        )
    }
}

// DarkModeScreen Composable function with theme switching capability
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkModeScreen(navController: NavController, themeViewModel: ThemeViewModel = viewModel()) {
    var selectedOption by remember { mutableStateOf(themeViewModel.themeMode) }
    val options = listOf("Device settings", "Dark mode", "Light mode")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dark Mode",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Choose how your mycareerportal experience looks for this device.",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Column(Modifier.selectableGroup()) {
                    options.forEachIndexed { index, option ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (themeViewModel.themeMode.ordinal == index),
                                    onClick = {
                                        val selectedMode = when (index) {
                                            0 -> ThemeMode.DEVICE
                                            1 -> ThemeMode.DARK
                                            2 -> ThemeMode.LIGHT
                                            else -> ThemeMode.DEVICE
                                        }
                                        themeViewModel.updateThemeMode(selectedMode)
                                        selectedOption = selectedMode
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedOption.ordinal == index),
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                BasicText(
                    text = "If you choose Device settings, this app will use the mode that’s already selected in this device’s settings.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }
    )
}
