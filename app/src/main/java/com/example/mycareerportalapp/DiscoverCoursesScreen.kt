package com.example.mycareerportalapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DiscoverCoursesScreen(navController: NavController) {
    val subjectOptions = listOf(
        "Mathematics", "Mathematical Literacy", "Physical Science", "CAT",
        "Life Orientation", "Home Languages", "Agriculture", "Life Sciences",
        "English", "History", "Geography", "Accounting", "Economics", "Business Studies",
        "Tourism", "Religion Studies", "EGD", "Consumer Studies"
    )

    val levelOptions = (1..100).toList()
    var selectedSubjects by remember { mutableStateOf(mutableListOf<Pair<String, Int>>()) }
    var calculatedAPS by remember { mutableStateOf(0) }
    var qualifiedCourses by remember { mutableStateOf(listOf<String>()) }
    var errorMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Select Subjects:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(7) {
                SubjectInputRow(
                    subjectOptions = subjectOptions,
                    levelOptions = levelOptions,
                    onSubjectAdded = { subject, level ->
                        if (subject.isNotEmpty() && level != null) {
                            // Check if the subject has already been added
                            if (selectedSubjects.any { it.first == subject }) {
                                errorMessage = "Subject has already been selected"
                                showError = true
                                return@SubjectInputRow
                            }
                            // Check for Mathematics and Mathematical Literacy conflict
                            if ((subject == "Mathematics" && selectedSubjects.any { it.first == "Mathematical Literacy" }) ||
                                (subject == "Mathematical Literacy" && selectedSubjects.any { it.first == "Mathematics" })
                            ) {
                                errorMessage = "You are not allowed to enter both Mathematics and Mathematical Literacy"
                                showError = true
                                return@SubjectInputRow
                            }
                            selectedSubjects.add(Pair(subject, level))
                            showError = false
                            println("Subject: $subject, Mark: $level")
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showError) {
            Text(text = errorMessage, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (selectedSubjects.isNotEmpty()) {
            Text(
                text = "Selected Subjects:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn {
                items(selectedSubjects) { (subject, level) ->
                    Text(text = "$subject - Marks: $level", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Button(
            onClick = {
                calculatedAPS = calculateAPSScore(selectedSubjects)
                qualifiedCourses = calculateQualifiedCourses(selectedSubjects, calculatedAPS)
            },
            enabled = selectedSubjects.size >= 6,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text(text = "Calculate APS and Find Courses")
        }

        if (calculatedAPS > 0) {
            Text(
                text = "Calculated APS: $calculatedAPS",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (qualifiedCourses.isNotEmpty()) {
            Text(
                text = "Qualified Courses:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn {
                items(qualifiedCourses) { course ->
                    Text(text = course, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        } else if (selectedSubjects.size >= 6 && qualifiedCourses.isEmpty()) {
            Text(
                text = "Sorry, you do not qualify for any course",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
@Composable
fun SubjectInputRow(
    subjectOptions: List<String>,
    levelOptions: List<Int>,
    onSubjectAdded: (String, Int?) -> Unit
) {
    var selectedSubject by remember { mutableStateOf("") }
    var expandedSubject by remember { mutableStateOf(false) }
    var selectedLevel by remember { mutableStateOf<Int?>(null) }
    var expandedLevel by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            OutlinedButton(onClick = { expandedSubject = true }, modifier = Modifier.fillMaxWidth()) {
                Text(text = if (selectedSubject.isNotEmpty()) selectedSubject else "Select Subject")
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(expanded = expandedSubject, onDismissRequest = { expandedSubject = false }) {
                subjectOptions.forEach { subject ->
                    DropdownMenuItem(text = { Text(subject) }, onClick = {
                        selectedSubject = subject
                        expandedSubject = false
                    })
                }
            }
        }

        Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            OutlinedButton(onClick = { expandedLevel = true }, modifier = Modifier.fillMaxWidth()) {
                Text(text = selectedLevel?.toString() ?: "Select Marks")
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(expanded = expandedLevel, onDismissRequest = { expandedLevel = false }) {
                levelOptions.forEach { level ->
                    DropdownMenuItem(text = { Text(level.toString()) }, onClick = {
                        selectedLevel = level
                        expandedLevel = false
                    })
                }
            }
        }

        IconButton(
            onClick = { onSubjectAdded(selectedSubject, selectedLevel) },
            enabled = selectedSubject.isNotEmpty() && selectedLevel != null
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Subject")
        }
    }
}

private fun calculateAPSScore(subjects: List<Pair<String, Int>>): Int {
    var totalScore = 0
    subjects.forEach { (_, marks) ->
        totalScore += when (marks) {
            in 0..29 -> 1
            in 30..39 -> 2
            in 40..49 -> 3
            in 50..59 -> 4
            in 60..69 -> 5
            in 70..79 -> 6
            in 80..100 -> 7
            else -> 0
        }
    }
    return totalScore
}

private fun calculateQualifiedCourses(subjects: List<Pair<String, Int>>, apsScore: Int): List<String> {
    val hasMathematics = subjects.any { it.first == "Mathematics" }
    val hasMathLit = subjects.any { it.first == "Mathematical Literacy" }
    val hasEng = subjects.any { it.first == "English" }
    val hasAgric = subjects.any { it.first == "Agriculture" }
    val hasLifeSc = subjects.any { it.first == "Life Sciences" }
    val hasGeo = subjects.any { it.first == "Geography" }
    val hasPhys = subjects.any { it.first == "Physical Science" }
    val qualifiedCourses = mutableListOf<String>()
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 40..100 })
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 50..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((apsScore in 26 ..49) && (hasEng && subjects.any { it.first == "English" && it.second in 50..100
            }))
        {
            //qualifiedCourses.add("You qualify to enroll under: ")
            qualifiedCourses.add("Diploma in Hospitality Management")
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 40..100})
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 50..100 })
        && (hasAgric && subjects.any { it.first == "Agriculture" && it.second in 50..100 })
    ) {
        if((hasEng && subjects.any { it.first == "English" && it.second in 50..100 }) && (hasAgric &&
                    subjects.any { it.first == "Agriculture" && it.second in 50..100 }))
        {
            if((apsScore in 25 ..49)) {
                // qualifiedCourses.add("You qualify to enroll under: ")
                qualifiedCourses.add("Diploma in Agriculture in Plant Production")
            }
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 30..100})
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 40..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((apsScore in 31 ..49) && (hasEng && subjects.any { it.first == "English" && it.second in 50..100
            }))
        {
            //qualifiedCourses.add("You qualify to enroll under: ")
            qualifiedCourses.add("Bachelor of Development Studies")
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 50..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((apsScore in 32 ..49) && (hasEng && subjects.any { it.first == "English" && it.second in 50..100
            }))
        {
            // qualifiedCourses.add("You qualify to enroll under: ")
            qualifiedCourses.add("Bachelor of Commerce")
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 50..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((apsScore in 26 ..49) && (hasEng && subjects.any { it.first == "English" && it.second in 50..100
            }))
        {
            // qualifiedCourses.add("You qualify to enroll under: ")
            qualifiedCourses.add("Diploma in ICT in Application Development")
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 50..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((apsScore in 32 ..49) && (hasEng && subjects.any { it.first == "English" && it.second in 50..100
            }))
        {
            //qualifiedCourses.add("You qualify to enroll under: ")
            qualifiedCourses.add("Bachelor in ICT in Application Development")
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 30..100})
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 40..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((hasLifeSc && subjects.any { it.first == "Life Sciences" && it.second in 50..100 }) || (hasGeo
                    && subjects.any { it.first == "Geography" && it.second in 50..100 }))
        {
            if((apsScore in 29 ..49) && (hasEng && subjects.any { it.first == "English" && it.second in
                        50..100 })) {
                qualifiedCourses.add("Diploma in Nature Conservation")
            }
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 50..100})
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 70..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((hasPhys && subjects.any { it.first == "Physical Science" && it.second in 50..100 }) || (hasAgric
                    && subjects.any { it.first == "Agriculture" && it.second in 50..100 }) || (hasLifeSc && subjects.any {
                it.first == "Life Sciences" && it.second in 50..100 }))
        {
            if((apsScore in 28 ..49) && (hasEng && subjects.any { it.first == "English" && it.second in
                        50..100 })) {
                qualifiedCourses.add("Bachelor of Agriculture in Agricultural Extension and Rural Resource Management")
            }
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 50..100})
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 70..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((hasEng && subjects.any { it.first == "English" && it.second in 50..100 }) && (hasLifeSc &&
                    subjects.any { it.first == "Life Sciences" && it.second in 50..100 }))
        {
            if((apsScore in 30 ..49)) {
                qualifiedCourses.add("Bachelor of Science in Agriculture")
            }
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 50..100})
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 70..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((hasEng && subjects.any { it.first == "English" && it.second in 50..100 }))
        {
            if((apsScore in 32 ..49)) {
                qualifiedCourses.add("Bachelor of Science Degree (BSc)")
            }
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 30..100})
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 50..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((hasEng && subjects.any { it.first == "English" && it.second in 50..100 }))
        {
            if((apsScore in 20 ..49)) {
                qualifiedCourses.add("Higher Certificate in Information Communication Technology in User Support")
            }
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 30..100})
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 40..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((hasEng && subjects.any { it.first == "English" && it.second in 50..100 }))
        {
            if((apsScore in 30 ..49)) {
                qualifiedCourses.add("Bachelor of Arts degree")
            }
        }
    }
    if ((hasMathematics && subjects.any { it.first == "Mathematics" && it.second in 40..100})
        || (hasMathLit && subjects.any { it.first == "Mathematical Literacy" && it.second in 50..100 })
        && (hasEng && subjects.any { it.first == "English" && it.second in 50..100 })
    ) {
        if((hasEng && subjects.any { it.first == "English" && it.second in 50..100 }))
        {
            if((apsScore in 28 ..49)) {
                qualifiedCourses.add(" Bachelor of Education in Foundation Phase Teaching")
            }
        }
    }
    println("You qualify to enroll under: ")
    return qualifiedCourses
}
