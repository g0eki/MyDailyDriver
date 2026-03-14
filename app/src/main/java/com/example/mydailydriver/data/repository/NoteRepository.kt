package com.example.mydailydriver.data.repository

import com.example.mydailydriver.data.models.Note
import com.example.mydailydriver.data.models.NoteGroup
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    val allNotes: Flow<List<Note>>

    suspend fun addNote(title: String, content: String)
    suspend fun deleteNote(id: String)
    suspend fun updateNote(id: String, newTitle: String, newContent: String)

    suspend fun getNoteById(id: String): Note?  // toDO(): Wenn man Room verwendet, kann drauf verzichten ?

    // Hier könnten später auch Gruppen-Funktionen hin:
    val allNoteGroups: Flow<List<NoteGroup>>
    suspend fun addNoteToGroup(name: String, description: String)
    suspend fun deleteNoteGroup(id: String)
    suspend fun updateNoteGroup(id: String, note: Note)
    suspend fun getNoteGroupById(id: String): NoteGroup?


}