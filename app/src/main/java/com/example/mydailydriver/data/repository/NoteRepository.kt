package com.example.mydailydriver.data.repository

import com.example.mydailydriver.data.models.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    val allNotes: Flow<List<Note>>

    suspend fun addNote(title: String, content: String)
    suspend fun deleteNote(id: String)
    suspend fun updateNote(id: String, newTitle: String, newContent: String)

    // Hier könnten später auch Gruppen-Funktionen hin:
    // suspend fun addNoteToGroup(groupId: String, note: Note)
}