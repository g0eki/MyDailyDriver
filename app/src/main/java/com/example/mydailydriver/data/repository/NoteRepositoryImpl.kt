package com.example.mydailydriver.data.repository

import com.example.mydailydriver.data.datastore.NotesStore
import com.example.mydailydriver.data.models.Note
import com.example.mydailydriver.data.models.NoteGroup
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

    override val allNoteGroups: Flow<List<NoteGroup>> = notesStore.notesGroup

    override suspend fun addNoteToGroup(name: String, description: String) {
        notesStore.addNoteGroup(name=name, description=description)
    }

    override suspend fun deleteNoteGroup(id: String) {
        notesStore.deleteNoteGroup(id=id)
    }

    override suspend fun updateNoteGroup(id: String, note: Note) {
        notesStore.updateNoteGroup(idGroup = id, note = note)
    }

    override suspend fun getNoteGroupById(id: String): NoteGroup? {
        return notesStore.getNoteGroupById(id)
    }

}