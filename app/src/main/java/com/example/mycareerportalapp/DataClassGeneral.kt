package com.example.mycareerportalapp
data class Message(
    val senderId: String = "",
    var senderProfileImage: String = "",
    var senderName: String = "",
    val receiverId: String = "",
    val messageText: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class UserProfile(
    val userId: String,
    val profilePictureUrl: String = "",
    val fullName: String = "",
    val role: String = "",
    val objective: String = "",
    val studentNumber: String?,
    val gender: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val education: List<Education> = emptyList(),
    val workExperience: List<WorkExperience> = emptyList(),
    val companyName: String = "",
    val skills: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val honorsAwards: List<String> = emptyList(),
    val certifications: List<String> = emptyList(),
    val interests: List<String> = emptyList(),
    val aboutText: String = ""
)

data class Education(
    val institutionName: String = "",
    val qualification: String = "",
    val gpa: String ="",
    val startYear: String = "",
    val graduationYear: String = ""
) {
    fun isValid(): Boolean {
        return institutionName.isNotEmpty() && qualification.isNotEmpty() && graduationYear.isNotEmpty()
    }
}

data class WorkExperience(
    val companyName: String,
    val jobTitle: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val role: String = "",
    val title: String = "",
    val startYear: String = "",
    val endYear: String = "",
    val responsibilities: List<String> = emptyList()
) {
    fun isValid(): Boolean {
        return companyName.isNotEmpty() && title.isNotEmpty() && startYear.isNotEmpty()
    }
}


open class BasePost(
    open val postId: String = "",
    open val title: String = "",
    open val description: String = "",
    open val imageUri: String = "",
    open val approved: Boolean = false
) {
    // Renamed method to avoid conflict
    open fun toCopy(
        postId: String = this.postId,
        title: String = this.title,
        description: String = this.description,
        imageUri: String = this.imageUri,
        approved: Boolean = this.approved
    ): BasePost {
        return BasePost(postId, title, description, imageUri, approved)
    }

    open val nodeName: String
        get() {
            return when (this) {
                is BursaryPost -> "bursaryPosts"
                is LearnershipPost -> "learnershipPosts"
                is JobPost -> "jobPosts"
                is InternshipPost -> "internshipPosts"
                is UpdatePost -> "updatePosts"
                else -> "genericPosts"
            }
        }
}



data class BursaryPost(
    override val postId: String = "",
    override val title: String = "",
    override val description: String = "",
    override val imageUri: String = "",
    override val approved: Boolean = false,
    val email: String = "",
    val url: String = ""
) : BasePost(postId, title, description, imageUri, approved) {
    override fun toCopy(
        postId: String,
        title: String,
        description: String,
        imageUri: String,
        approved: Boolean
    ): BursaryPost {
        return BursaryPost(postId, title, description, imageUri, approved)
    }
}

// Repeat similarly for LearnershipPost, JobPost, InternshipPost, UpdatePost...

data class LearnershipPost(
    override val postId: String = "",
    val learnershipTitle: String = "",
    val learnershipDescription: String = "",
    override val imageUri: String = "",
    override val approved: Boolean = false,
    val email: String = "",
    val url: String = ""
) : BasePost(postId, learnershipTitle, learnershipDescription, imageUri, approved)

data class UpdatePost(
    override val postId: String = "",
    val updateTitle: String = "",
    val updateDescription: String = "",
    override val approved: Boolean = false
) : BasePost(postId, updateTitle, updateDescription)

data class JobPost(
    override val postId: String = "",
    val jobTitle: String = "",
    val jobDescription: String = "",
    override val imageUri: String = "",
    override val approved: Boolean = false,
    val email: String = "",
    val url: String = ""
) : BasePost(postId, jobTitle, jobDescription, imageUri, approved)

data class InternshipPost(
    override val postId: String = "",
    val internshipTitle: String = "",
    val internshipDescription: String = "",
    override val imageUri: String = "",
    override val approved: Boolean = false,
    val email: String = "",
    val url: String = ""
) : BasePost(postId, internshipTitle, internshipDescription, imageUri, approved)
