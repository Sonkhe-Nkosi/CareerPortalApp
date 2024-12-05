import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Chat(
    val userName: String,
    val profession: String,
    val lastMessage: String,
    val timestamp: Long
)

suspend fun fetchRecentChats(): List<Chat> {
    val db = FirebaseFirestore.getInstance()
    val chats = mutableListOf<Chat>()

    val querySnapshot = db.collection("chats")
        .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
        .limit(3)
        .get()
        .await()

    for (document in querySnapshot.documents) {
        val chat = document.toObject(Chat::class.java)
        if (chat != null) {
            chats.add(chat)
        }
    }

    return chats
}
