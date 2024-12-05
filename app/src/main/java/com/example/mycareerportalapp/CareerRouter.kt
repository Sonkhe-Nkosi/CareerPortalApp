import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mycareerportalapp.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

@Composable
fun NavigationSystem(start: String, navController: NavHostController) {
    val navController = rememberNavController() // No need to pass it from the parent
    NavHost(navController = navController, startDestination = start) {


        composable("UserLoginScreen") {
            UserLoginScreen(navController)
        }
        composable("UserSignUpScreen") {
            UserSignUpScreen(navController)
        }
        composable("ApplicantDashboardScreen") {
            ApplicantDashboardScreen(navController)
        }
        composable("HomeScreen") {
            HomeScreen(navController)
        }
        composable("MyProfileScreen") {
            MyProfileScreen(navController)
        }
        composable("LoadingScreen") {
            LoadingScreen(navController)
        }
        composable("UndergraduatesDashboardScreen") {
            UndergraduatesDashboardScreen(navController)
        }
        composable("CreatePosterScreen/{posterId}/{applicantId}",
            arguments = listOf(
                navArgument("posterId") { type = NavType.StringType },
                navArgument("applicantId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val posterId = backStackEntry.arguments?.getString("posterId") ?: ""
            CreatePosterScreen(navController, posterId)
        }
        composable("PosterHistoryScreen"
        ) {
            PosterHistoryScreen(navController)
        }

        composable("ApplicantListScreen/{posterId}/{applicantId}",
            arguments = listOf(
                navArgument("posterId") { type = NavType.StringType },
                navArgument("applicantId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val posterId = backStackEntry.arguments?.getString("posterId") ?: ""
            val applicantId = backStackEntry.arguments?.getString("applicantId") ?: ""
            ApplicantListScreen(navController)
        }
        composable("UpdateApplicationStatusScreen/{posterId}/{applicantId}",
            arguments = listOf(
                navArgument("posterId") { type = NavType.StringType },
                navArgument("applicantId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val posterId = backStackEntry.arguments?.getString("posterId") ?: ""
            val applicantId = backStackEntry.arguments?.getString("applicantId") ?: ""
            UpdateApplicationStatusScreen(navController, posterId)
        }



        composable("AdminDashboardScreen") {
            AdminDashboardScreen(navController)
        }
        composable("JobPostingScreen") {
            JobPostingScreen(navController)
        }
        composable("LearnershipPostingScreen") {
            LearnershipPostingScreen(navController)
        }
        composable("UpdatePostingScreen") {
            UpdatePostingScreen(navController)
        }
        composable("EmployerDashboardScreen"){
            EmployerDashboardScreen(navController, employerId = "")
        }


        // Define the composable with a route and placeholders for parameters
        composable("ChatScreen/{contactName}/{contactProfileImage}") { backStackEntry ->
            // Extract parameters from the backStackEntry
            val currentUserId: String = backStackEntry.arguments?.getString("currentUserId") ?:""
            val contactUserId: String = backStackEntry.arguments?.getString("contactUserId") ?:""
            val email = backStackEntry.arguments?.getString("email") ?: ""

            ChatScreen(

                currentUserId = currentUserId,
                contactUserId = contactUserId,
                navController = navController,
                email = email
            )
        }

        composable(
            "ViewPostScreen/{postId}/{postTitle}/{postDescription}/{postImageUri}",
            arguments = listOf(
                navArgument("postId") { type = NavType.StringType },
                navArgument("postTitle") { type = NavType.StringType },
                navArgument("postDescription") { type = NavType.StringType },
                navArgument("postImageUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val postTitle = backStackEntry.arguments?.getString("postTitle") ?: ""
            val postDescription = backStackEntry.arguments?.getString("postDescription") ?: ""
            val postImageUri = backStackEntry.arguments?.getString("postImageUri") ?: ""
            ViewPostScreen(navController, postId, postTitle, postDescription, postImageUri)
        }

        composable("UserListScreen") {
            val loggedInUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            UserListScreen(navController, loggedInUserId)
        }
        composable("InternshipPostingScreen") {
            InternshipPostingScreen(navController)
        }
        composable("TrackMyStatusScreen") {
            TrackMyStatusScreen(navController)
        }
        composable("DiscoverCoursesScreen") {
            DiscoverCoursesScreen(navController)
        }
        composable("UploadResumeScreen") {
            UploadResumeScreen(navController)
        }
        composable("BursaryPostingScreen") {
            BursaryPostingScreen(navController)
        }
        composable("SearchFAQsScreen") {
            SearchFAQsScreen(navController)
        }
        composable("AboutAppScreen") {
            AboutAppScreen(navController)
        }
        composable("GeneralSettingsScreen") {
            GeneralSettingsScreen(navController)
        }
        composable("SecurityAndPrivacyScreen") {
            SecurityAndPrivacyScreen(navController)
        }

        composable(
            route = "ApplicationFormScreen/{postId}/{postTitle}",
            arguments = listOf(
                navArgument("postId") { type = NavType.StringType },
                navArgument("postTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val postTitle = backStackEntry.arguments?.getString("postTitle") ?: ""
            ApplicationFormScreen(navController, postId, postTitle)
        }


        composable("ForgotPasswordScreen") {
            ForgotPasswordScreen(navController)
        }
        composable("CareerGuidanceScreen") {
            CareerGuidanceScreen(navController)
        }

        composable("ResumeFormScreen") {
            ResumeFormScreen(navController)
        }

        composable("DarkModeScreen") {
            DarkModeScreen(navController)
        }
        composable("DemographicInformationScreen") {
            DemographicInformationScreen(navController)
        }
    }
}
suspend fun fetchPostData(postId: String): BasePost? {
    val database = FirebaseDatabase.getInstance()
    val postReference = database.getReference("approvedgenericPostss").child(postId)

    // Retrieve data
    val dataSnapshot = postReference.get().await()
    return dataSnapshot.getValue(BasePost::class.java) // Adjust if using specific subclasses
}
