package com.example.mydailydriver.ui.elements.edit

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydailydriver.data.repository.NoteRepository
import kotlinx.coroutines.launch

class EditViewModel(
    private val repository: NoteRepository,
    private val noteId: String?=null,
) : ViewModel() {


    var title by mutableStateOf("") // waurm kein: var title by rememberSaveable() { mutableStateOf("") }
    var content by mutableStateOf("")

    init {
        Log.d("DEBUG", "EditViewModel init, noteId: $noteId")  // ✅
        if (noteId != null) {
            viewModelScope.launch {
                val note = repository.getNoteById(noteId)
                Log.d("DEBUG", "Note geladen: $note")  // ✅
                if (note != null) {
                    title = note.title
                    content = note.content
                }
            }
        }
    }


    // Create and Delete
    fun addNote(newTitel: String, newNote: String) {
        viewModelScope.launch {
            // my_store_notes.addNote(title = newTitel, content = newNote)
            repository.addNote(title = newTitel, content = newNote)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            repository.deleteNote(id = id)
        }
    }

    // setter
    fun updateNote(id: String, newTitel: String, newContent: String) {
        viewModelScope.launch {
            repository.updateNote(id = id, newTitle = newTitel, newContent = newContent)
        }
    }
}