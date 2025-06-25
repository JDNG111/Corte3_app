// ForumModels.kt
package com.example.corte1_app.models

import com.google.firebase.Timestamp

// Reutilizable entre screens

data class ForumPost(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val author: String = "",
    val isClosed: Boolean = false,
    val timestamp: Timestamp = Timestamp.now()
)

data class Reply(
    val id: String = "",
    val text: String = "",
    val author: String = "",
    val likes: Int = 0,
    val likedBy: List<String> = emptyList(),
    val timestamp: Timestamp = Timestamp.now()
)
