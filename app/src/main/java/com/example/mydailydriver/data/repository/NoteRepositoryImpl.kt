package com.example.mydailydriver.data.repository

import com.example.mydailydriver.data.datastore.NotesStore
import com.example.mydailydriver.data.models.Note
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val notesStore: NotesStore
) : NoteRepository {

    // Wir leiten den Flow vom Store einfach weiter
    override val allNotes: Flow<List<Note>> = notesStore.notes

    override suspend fun addNote(title: String, content: String) {
        notesStore.addNote(title, content)
    }

    override suspend fun deleteNote(id: String) {
        notesStore.deleteNote(id)
    }

    override suspend fun updateNote(id: String, newTitle: String, newContent: String) {
        notesStore.updateNote(id, newTitle=newTitle, newContent=newContent)
    }

    override suspend fun getNoteById(id: String): Note? {
        return notesStore.getNoteById(id)
    }

}