package com.example.mycareerportalapp

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import java.io.InputStream

fun uploadFile(uri: Uri, context: Context, navController: NavController) {
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        // Read the input stream to get file data
        val fileData = inputStream?.readBytes()
        inputStream?.close()

        // Simulate file upload to server or database
        // You can replace this with actual file upload logic to your backend
        if (fileData != null) {
            Log.d("FileUpload", "File data length: ${fileData.size}")
            // Navigate back to HomeScreen or show success message

        } else {
            Toast.makeText(context, "Failed to read file data", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Log.e("FileUpload", "Error uploading file", e)
        Toast.makeText(context, "Error uploading file", Toast.LENGTH_SHORT).show()
    }
}

fun uploadFileAdmin(uri: Uri, context: Context, navController: NavController) {
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        // Read the input stream to get file data
        val fileData = inputStream?.readBytes()
        inputStream?.close()

        // Simulate file upload to server or database
        // You can replace this with actual file upload logic to your backend
        if (fileData != null) {
            Log.d("FileUpload", "File data length: ${fileData.size}")
            // Navigate back to AdminDashboard or show success message
            Toast.makeText(context, "File uploaded successfully", Toast.LENGTH_SHORT).show()
            navController.navigate("AdminDashboard") {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        } else {
            Toast.makeText(context, "Failed to read file data", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Log.e("FileUpload", "Error uploading file", e)
        Toast.makeText(context, "Error uploading file", Toast.LENGTH_SHORT).show()
    }
}
