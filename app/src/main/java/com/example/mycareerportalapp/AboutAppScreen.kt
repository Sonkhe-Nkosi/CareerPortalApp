package com.example.mycareerportalapp


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen(navController: NavController) {
    Scaffold(
        topBar = { GenericTopBar(navController, "About App") },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    ParagraphSection(
                        heading = "About App",
                        content = "Welcome to the University of Mpumalanga Career Portal – your gateway to opportunities! This app is designed to bridge the gap between " +
                                "graduates and employers, providing a seamless platform for both to connect and grow. For graduates, the app offers an intuitive interface to " +
                                "explore job listings, internships, and learnerships, and apply directly to their desired roles. Employers, on the other hand, can easily post" +
                                "vacancies, search for qualified candidates, and manage applications all in one place. Our goal is to simplify the hiring process, " +
                                "empowering graduates to kick-start their careers and helping employers find the talent they need."
                    )
                }
                item {
                    ParagraphSection(
                        heading = "Privacy Policy",
                        content = "Last Updated: 30 August 2024\n" +
                                "\n" +
                                "Your privacy is important to us, and we are committed to protecting your personal information. This Privacy Policy outlines how we collect, use, share, and protect your data when you use our app. By accessing or using our app, you agree to the terms of this Privacy Policy.\n" +
                                "\n" +
                                "1. Information We Collect\n" +
                                "We may collect the following types of information:\n" +
                                "\n" +
                                "Personal Information: When you create an account, apply for a job, or post a vacancy, we collect personal details such as your name, email address, contact number, education, work experience, and any other information you provide in your profile or applications.\n" +
                                "Usage Data: We collect data about how you use the app, such as your activity, interactions, preferences, and device information (e.g., IP address, browser type, and operating system).\n" +
                                "Cookies and Similar Technologies: We use cookies and similar tracking technologies to improve your experience on our app, personalize content, and analyze usage patterns.\n" +
                                "2. How We Use Your Information\n" +
                                "We use your information for the following purposes:\n" +
                                "\n" +
                                "To facilitate the connection between graduates and employers, including processing job applications and managing job postings.\n" +
                                "To improve and personalize your experience on the app.\n" +
                                "To communicate with you about your account, applications, or job postings.\n" +
                                "To analyze app usage and improve our services.\n" +
                                "To comply with legal obligations and protect our rights.\n" +
                                "3. Sharing Your Information\n" +
                                "We may share your information with:\n" +
                                "\n" +
                                "Employers: If you are a graduate applying for a job, we will share your profile and application details with the employers you are applying to.\n" +
                                "Service Providers: We may share your information with third-party service providers who assist us in operating the app, such as hosting, analytics, and payment processing.\n" +
                                "Legal Authorities: We may disclose your information if required by law or in response to valid requests by public authorities.\n" +
                                "4. Your Privacy Rights\n" +
                                "You have the following rights regarding your personal information:\n" +
                                "\n" +
                                "Access: You can request access to the personal information we hold about you.\n" +
                                "Correction: You can request corrections to any inaccurate or incomplete personal information.\n" +
                                "Deletion: You can request that we delete your personal information, subject to certain conditions.\n" +
                                "Objection: You can object to the processing of your personal information for certain purposes.\n" +
                                "5. Security of Your Information\n" +
                                "We take appropriate technical and organizational measures to protect your personal information from unauthorized access, disclosure, or misuse. However, please note that no method of transmission over the internet or electronic storage is completely secure.\n" +
                                "\n" +
                                "6. Children’s Privacy\n" +
                                "Our app is not intended for children under the age of 16. We do not knowingly collect personal information from children under 16. If you are a parent or guardian and believe that your child has provided us with personal information, please contact us immediately.\n" +
                                "\n" +
                                "7. Changes to This Privacy Policy\n" +
                                "We may update this Privacy Policy from time to time. Any changes will be posted on this page, and the \"Last Updated\" date will be revised accordingly. We encourage you to review this Privacy Policy periodically to stay informed about how we are protecting your information.\n" +
                                "\n" +
                                "8. Contact Us\n" +
                                "If you have any questions or concerns about this Privacy Policy or our data practices, please contact us at [contact email or phone number]."
                    )
                }
                item {
                    ParagraphSection(
                        heading = "Terms and Conditions",
                        content = "Last Updated: 30 August 2024\n" +
                                "\n" +
                                "(\"Terms\") govern your use of our app, services, and any associated content. By accessing or using our app, you agree to comply with and be bound by these Terms. If you do not agree with any part of these Terms, please do not use our app.\n" +
                                "\n" +
                                "1. Acceptance of Terms\n" +
                                "By using the University of Mpumalanga Career Portal, you acknowledge that you have read, understood, and agree to be bound by these Terms. We may update these Terms from time to time, and it is your responsibility to review them regularly. Your continued use of the app following any changes indicates your acceptance of the revised Terms.\n" +
                                "\n" +
                                "2. Use of the App\n" +
                                "Eligibility: The app is intended for use by graduates, students, and employers. You must be at least 16 years old to create an account and use the app.\n" +
                                "Account Registration: To access certain features of the app, you may be required to create an account and provide accurate and complete information. You are responsible for maintaining the confidentiality of your account and password and for any activities that occur under your account.\n" +
                                "Prohibited Activities: You agree not to:\n" +
                                "Use the app for any unlawful, fraudulent, or malicious purposes.\n" +
                                "Post, share, or transmit content that is defamatory, obscene, offensive, or otherwise inappropriate.\n" +
                                "Impersonate any person or entity or misrepresent your affiliation with any person or entity.\n" +
                                "Attempt to gain unauthorized access to any part of the app, other users' accounts, or our servers.\n" +
                                "Disrupt or interfere with the operation or security of the app.\n" +
                                "3. User Content\n" +
                                "Content Ownership: You retain ownership of any content you submit or post to the app, including job applications, resumes, and job listings. By submitting content, you grant us a non-exclusive, royalty-free, worldwide license to use, display, reproduce, and distribute your content for the purpose of providing and promoting the app.\n" +
                                "Responsibility: You are solely responsible for the content you submit or post on the app. We do not endorse or assume any responsibility for any user content.\n" +
                                "4. Job Applications and Postings\n" +
                                "Graduates: You may use the app to search for job opportunities, internships, and learnerships and to apply for positions directly through the app. It is your responsibility to ensure that the information you provide is accurate and up-to-date.\n" +
                                "Employers: Employers may use the app to post job vacancies and review applications. Employers are responsible for ensuring that all job postings are accurate, lawful, and in compliance with applicable laws and regulations.\n" +
                                "5. Termination\n" +
                                "We reserve the right to suspend or terminate your access to the app, without notice, for any reason, including but not limited to a breach of these Terms. Upon termination, your right to use the app will immediately cease.\n" +
                                "\n" +
                                "6. Intellectual Property Rights\n" +
                                "All content, features, and functionality of the app (including text, graphics, logos, icons, and software) are owned by or licensed to the University of Mpumalanga and are protected by copyright, trademark, and other intellectual property laws. You may not use, reproduce, or distribute any content without our express written permission.\n" +
                                "\n" +
                                "7. Disclaimer of Warranties\n" +
                                "The app is provided \"as is\" and \"as available\" without any warranties of any kind, either express or implied. We do not guarantee that the app will be error-free, secure, or uninterrupted, or that any defects will be corrected. Your use of the app is at your own risk.\n" +
                                "\n" +
                                "8. Limitation of Liability\n" +
                                "To the fullest extent permitted by law, the University of Mpumalanga and its affiliates, officers, employees, and agents shall not be liable for any direct, indirect, incidental, consequential, or punitive damages arising out of or related to your use or inability to use the app, including but not limited to loss of data, profits, or goodwill.\n" +
                                "\n" +
                                "9. Governing Law\n" +
                                "These Terms are governed by and construed in accordance with the laws of South Africa. Any disputes arising out of or in connection with these Terms shall be subject to the exclusive jurisdiction of the courts of South Africa.\n" +
                                "\n" +
                                "10. Changes to the Terms\n" +
                                "We reserve the right to modify these Terms at any time. Any changes will be posted on this page, and the \"Last Updated\" date will be revised accordingly. It is your responsibility to review these Terms regularly to stay informed about any updates."
                    )
                }

            }
        }
    )
}
@Composable
fun ParagraphSection(heading: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = heading,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppTopBar() {
    TopAppBar(title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "About App",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
            )
        }
    },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.Black
        )
    )
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTopBar(navController: NavController, title: String) {
    TopAppBar(
        title = { Text(title, color = Color.Black) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF3F4F6),
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
        ),
        actions = {
            IconButton(onClick = { /* TODO: Implement Settings functionality */ }) {
                Icon(painter = painterResource(id = R.drawable.logo), contentDescription = "Settings")
            }
        }
    )
}
