package com.techsavvy.notesapp.helpers
data class Note(
    val id: Long, // Unique identifier for each note
    val title: String,
    val content: String,
    val timestamp: Long
)
