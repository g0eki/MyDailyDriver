package com.example.mydailydriver.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mydailydriver.data.models.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.first  // ✅ NEU

private val Context.notesDataStore by preferencesDataStore(name = "notes-store")

class NotesStore(private val context: Context) {

    // Alle Notizen als Flow
    val notes: Flow<List<Note>> = context.notesDataStore.data
        .map { preferences ->
            val json = preferences[notesKey] ?: "[]"
            Json.decodeFromString<List<Note>>(json)  // JSON → Liste
        }

    // Notiz hinzufügen
    suspend fun addNote(title: String, content: String) {
        context.notesDataStore.edit { preferences ->
            val json = preferences[notesKey] ?: "[]"
            val currentNotes = Json.decodeFromString<List<Note>>(json).toMutableList()
            currentNotes.add(Note(title = title, content = content))
            preferences[notesKey] = Json.encodeToString(currentNotes)  // Liste → JSON
        }
    }

    // Notiz löschen (per ID)
    suspend fun deleteNote(id: String) {
        context.notesDataStore.edit { preferences ->
            val json = preferences[notesKey] ?: "[]"
            val currentNotes = Json.decodeFromString<List<Note>>(json).toMutableList()
            currentNotes.removeIf { it.id == id }
            preferences[notesKey] = Json.encodeToString(currentNotes)
        }
    }

    // Notiz bearbeiten (per ID)
    suspend fun updateNote(id: String, newTitle: String, newContent: String) {
        context.notesDataStore.edit { preferences ->
            val json = preferences[notesKey] ?: "[]"
            val currentNotes = Json.decodeFromString<List<Note>>(json).toMutableList()
            val index = currentNotes.indexOfFirst { it.id == id }
            if (index != -1) {
                currentNotes[index] = currentNotes[index].copy(
                    title = newTitle,
                    content = newContent
                )
            }
            preferences[notesKey] = Json.encodeToString(currentNotes)
        }
    }

    suspend fun getNoteById(id: String): Note? {
        val json = context.notesDataStore.data.first()[notesKey] ?: "[]"
        return Json.decodeFromString<List<Note>>(json).find { it.id == id }
    }

    companion object {
        private val notesKey = stringPreferencesKey("notes")
    }
}