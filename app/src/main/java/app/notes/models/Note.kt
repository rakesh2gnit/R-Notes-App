package app.notes.models

import com.google.firebase.Timestamp

data class Note(
    val title : String,
    val content : String,
    val timestamp : Timestamp,
)



