package com.example.mydailydriver.data.datastore

import java.util.UUID
import kotlinx.serialization.Serializable

// Datenklasse für eine Notiz
@Serializable  // ← Das braucht kotlinx-serialization!
data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String
)
